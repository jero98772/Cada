import { Request } from 'express'
import { forward } from './spring-client'

export async function list(req: Request) {
	return forward(req, 'GET', '/arbitros')
}

export async function getById(req: Request, id: string) {
	return forward(req, 'GET', `/arbitros/${id}`)
}

export async function create(req: Request) {
	return forward(req, 'POST', '/arbitros', req.body)
}

export async function update(req: Request, id: string) {
	return forward(req, 'PUT', `/arbitros/${id}`, req.body)
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
