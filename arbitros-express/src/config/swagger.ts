import { env } from './env'

export const swaggerSpec = {
	openapi: '3.0.3',
	info: {
		title: 'Árbitros API (Express)',
		version: '1.0.0',
		description:
			'API REST para árbitros que consume la API de Spring Boot del Entregable 2.',
	},
	servers: [
		{ url: `http://localhost:${env.PORT}`, description: 'Local' },
	],
	paths: {
		'/health': {
			get: {
				summary: 'Health check',
				responses: { '200': { description: 'OK' } },
			},
		},
		'/api/auth/register': {
			post: {
				summary: 'Registro de árbitro',
				requestBody: { required: true },
				responses: { '201': { description: 'Creado' } },
			},
		},
		'/api/auth/login': {
			post: {
				summary: 'Login de árbitro',
				requestBody: { required: true },
				responses: { '200': { description: 'OK' } },
			},
		},
		'/api/auth/me': {
			get: {
				summary: 'Perfil del árbitro autenticado',
				security: [{ bearerAuth: [] }],
				responses: { '200': { description: 'OK' } },
			},
		},
		'/api/arbitros': {
			get: { summary: 'Lista de árbitros', responses: { '200': { description: 'OK' } } },
			post: { summary: 'Crear árbitro', responses: { '201': { description: 'Creado' } } },
		},
		'/api/arbitros/{id}': {
			get: {
				summary: 'Detalle de árbitro',
				parameters: [
					{ in: 'path', name: 'id', required: true, schema: { type: 'string' } },
				],
				responses: { '200': { description: 'OK' } },
			},
			put: {
				summary: 'Actualizar árbitro',
				parameters: [
					{ in: 'path', name: 'id', required: true, schema: { type: 'string' } },
				],
				responses: { '200': { description: 'OK' } },
			},
		},
		'/api/arbitros/{id}/partidos': {
			get: {
				summary: 'Partidos asignados',
				parameters: [
					{ in: 'path', name: 'id', required: true, schema: { type: 'string' } },
				],
				responses: { '200': { description: 'OK' } },
			},
		},
		'/api/arbitros/{id}/liquidaciones': {
			get: {
				summary: 'Liquidaciones',
				parameters: [
					{ in: 'path', name: 'id', required: true, schema: { type: 'string' } },
				],
				responses: { '200': { description: 'OK' } },
			},
		},
		'/api/arbitros/{id}/dashboard': {
			get: {
				summary: 'Dashboard del árbitro',
				parameters: [
					{ in: 'path', name: 'id', required: true, schema: { type: 'string' } },
				],
				responses: { '200': { description: 'OK' } },
			},
		},
		'/api/uploads/avatar': {
			post: {
				summary: 'Subir avatar a S3 (público)',
				requestBody: { required: true },
				responses: { '200': { description: 'URL pública' } },
			},
		},
	},
	components: {
		securitySchemes: {
			bearerAuth: {
				type: 'http',
				scheme: 'bearer',
				bearerFormat: 'JWT',
			},
		},
	},
} as const
