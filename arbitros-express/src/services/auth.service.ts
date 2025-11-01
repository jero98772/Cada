import { Request } from 'express'
import { forward } from './spring-client'

export async function register(req: Request) {
	return forward(req, 'POST', '/auth/register', req.body)
}

export async function login(req: Request) {
	return forward(req, 'POST', '/auth/login', req.body)
}

export async function me(req: Request) {
	return forward(req, 'GET', '/auth/me')
}
