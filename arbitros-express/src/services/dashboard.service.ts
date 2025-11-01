import { Request } from 'express'
import { forward } from './spring-client'

export async function getDashboard(req: Request, id: string) {
	return forward(req, 'GET', `/arbitros/${id}/dashboard`)
}
