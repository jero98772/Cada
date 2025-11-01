"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const dotenv_1 = __importDefault(require("dotenv"));
const env_1 = require("../src/config/env");
const axios_1 = __importDefault(require("axios"));
const uuid_1 = require("uuid");
const s3_service_1 = require("../src/services/s3.service");
dotenv_1.default.config();
const client = axios_1.default.create({ baseURL: env_1.env.SPRING_API_BASE_URL, timeout: 15000 });
if (process.env.SEED_BEARER) {
    client.interceptors.request.use((cfg) => {
        cfg.headers = cfg.headers || {};
        cfg.headers['Authorization'] = `Bearer ${process.env.SEED_BEARER}`;
        return cfg;
    });
}
const firstNames = [
    'Carlos',
    'Juan',
    'María',
    'Ana',
    'Luis',
    'Pedro',
    'Laura',
    'Sofía',
    'Miguel',
    'Andrés',
];
const lastNames = [
    'García',
    'Rodríguez',
    'Gómez',
    'Martínez',
    'Fernández',
    'López',
    'Díaz',
    'Sánchez',
    'Pérez',
    'Ramírez',
];
const categorias = ['A', 'B', 'C'];
function pick(arr) {
    return arr[Math.floor(Math.random() * arr.length)];
}
async function getPlaceholderBuffer() {
    const res = await axios_1.default.get(`https://picsum.photos/seed/${(0, uuid_1.v4)()}/256/256.jpg`, { responseType: 'arraybuffer' });
    return Buffer.from(res.data);
}
function buildArbitro(i, avatarUrl) {
    const nombre = pick(firstNames);
    const apellido = pick(lastNames);
    const email = `${nombre.toLowerCase()}.${apellido.toLowerCase()}${i}@example.com`;
    return {
        nombre,
        apellido,
        email,
        telefono: `+57${Math.floor(3000000000 + Math.random() * 99999999)}`,
        categoria: pick(categorias),
        avatarUrl,
    };
}
async function seed(count = 8) {
    console.log(`Seeding ${count} árbitros...`);
    for (let i = 0; i < count; i++) {
        try {
            const buf = await getPlaceholderBuffer();
            const key = `arbitros/seeds/${(0, uuid_1.v4)()}.jpg`;
            const avatarUrl = await (0, s3_service_1.uploadPublic)({ buffer: buf, key, contentType: 'image/jpeg' });
            const payload = buildArbitro(i + 1, avatarUrl);
            const { data } = await client.post('/arbitros', payload);
            console.log(`✔️  creado: ${payload.nombre} ${payload.apellido}`, data?.id || '');
        }
        catch (err) {
            console.error('✖️  error seeding árbitro', err?.response?.data || err?.message);
        }
    }
    console.log('Seed complete');
}
seed().catch((e) => {
    console.error(e);
    process.exit(1);
});
