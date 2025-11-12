import { Request } from 'express'
import { forward } from './spring-client'

export async function list(req: Request) {
	return forward(req, 'GET', '/api/v1/arbitros')
}

export async function getById(req: Request, id: string) {
	return forward(req, 'GET', `/api/v1/arbitros/${id}`)
}

export async function create(req: Request) {
	return forward(req, 'POST', '/api/v1/arbitros', req.body)
}

export async function update(req: Request, id: string) {
	return forward(req, 'PUT', `/api/v1/arbitros/${id}`, req.body)
}

export async function getPartidos(req: Request, id: string) {
	return forward(req, 'GET', `/arbitros/${id}/partidos`)
}

export async function getLiquidaciones(req: Request, id: string) {
	return forward(req, 'GET', `/arbitros/${id}/liquidaciones`)
}

export async function getDashboard(req: Request, id: string) {
	return forward(req, 'GET', `/arbitros/${id}/dashboard`)
}
