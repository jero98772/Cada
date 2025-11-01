import { Request, Response } from 'express'
import * as arbitrosService from '../services/arbitros.service'
import { asyncHandler } from '../middleware/error-handler'

export const list = asyncHandler(async (req: Request, res: Response) => {
	const data = await arbitrosService.list(req)
	res.json(data)
})

export const getById = asyncHandler(async (req: Request, res: Response) => {
	const data = await arbitrosService.getById(req, req.params.id)
	res.json(data)
})

export const create = asyncHandler(async (req: Request, res: Response) => {
	const data = await arbitrosService.create(req)
	res.status(201).json(data)
})

export const update = asyncHandler(async (req: Request, res: Response) => {
	const data = await arbitrosService.update(req, req.params.id)
	res.json(data)
})

export const getPartidos = asyncHandler(async (req: Request, res: Response) => {
	const data = await arbitrosService.getPartidos(req, req.params.id)
	res.json(data)
})

export const getLiquidaciones = asyncHandler(async (req: Request, res: Response) => {
	const data = await arbitrosService.getLiquidaciones(req, req.params.id)
	res.json(data)
})

export const getDashboard = asyncHandler(async (req: Request, res: Response) => {
	const data = await arbitrosService.getDashboard(req, req.params.id)
	res.json(data)
})
