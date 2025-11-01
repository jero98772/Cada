import { Request } from 'express'
import axios, { Method } from 'axios'
import { env } from '../config/env'

const client = axios.create({ baseURL: env.SPRING_API_BASE_URL, timeout: 15000 })

export async function forward<T>(
	req: Request,
	method: Method,
	path: string,
	data?: any,
	params?: any,
): Promise<T> {
	const headers: Record<string, string> = {}
	const auth = req.headers['authorization']
	if (typeof auth === 'string') headers['authorization'] = auth

	const res = await client.request<T>({ url: path, method, data, params, headers })
	return res.data
}
