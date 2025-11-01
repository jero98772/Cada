import { Request, Response } from 'express'
import * as authService from '../services/auth.service'
import { asyncHandler } from '../middleware/error-handler'

export const register = asyncHandler(async (req: Request, res: Response) => {
	const data = await authService.register(req)
	res.status(201).json(data)
})

export const login = asyncHandler(async (req: Request, res: Response) => {
	const data = await authService.login(req)
	res.json(data)
})

export const me = asyncHandler(async (req: Request, res: Response) => {
	const data = await authService.me(req)
	res.json(data)
})
