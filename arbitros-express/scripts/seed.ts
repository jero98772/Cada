import dotenv from 'dotenv'
import { env } from '../src/config/env'
import axios from 'axios'
import { v4 as uuid } from 'uuid'
import { uploadPublic } from '../src/services/s3.service'

dotenv.config()

const client = axios.create({ baseURL: env.SPRING_API_BASE_URL, timeout: 15000 })

if (process.env.SEED_BEARER) {
	client.interceptors.request.use((cfg) => {
		cfg.headers = cfg.headers || {}
		cfg.headers['Authorization'] = `Bearer ${process.env.SEED_BEARER}`
		return cfg
	})
}

type ArbitroInput = {
	nombre: string
	apellido: string
	email: string
	telefono?: string
	categoria?: string
	avatarUrl?: string
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
]
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
]
const categorias = ['A', 'B', 'C']

function pick<T>(arr: T[]) {
	return arr[Math.floor(Math.random() * arr.length)]
}

async function getPlaceholderBuffer() {
	const res = await axios.get(
		`https://picsum.photos/seed/${uuid()}/256/256.jpg`,
		{ responseType: 'arraybuffer' },
	)
	return Buffer.from(res.data)
}

function buildArbitro(i: number, avatarUrl: string): ArbitroInput {
	const nombre = pick(firstNames)
	const apellido = pick(lastNames)
	const email = `${nombre.toLowerCase()}.${apellido.toLowerCase()}${i}@example.com`
	return {
		nombre,
		apellido,
		email,
		telefono: `+57${Math.floor(3000000000 + Math.random() * 99999999)}`,
		categoria: pick(categorias),
		avatarUrl,
	}
}

async function seed(count = 8) {
	console.log(`Seeding ${count} árbitros...`)
	for (let i = 0; i < count; i++) {
		try {
			const buf = await getPlaceholderBuffer()
			const key = `arbitros/seeds/${uuid()}.jpg`
			const avatarUrl = await uploadPublic({ buffer: buf, key, contentType: 'image/jpeg' })
			const payload = buildArbitro(i + 1, avatarUrl)
			const { data } = await client.post('/arbitros', payload)
			console.log(`created: ${payload.nombre} ${payload.apellido}`, data?.id || '')
		} catch (err: any) {
			console.error('error seeding arbitro', err?.response?.data || err?.message)
		}
	}
	console.log('Seed complete')
}

seed().catch((e) => {
	console.error(e)
	process.exit(1)
})
