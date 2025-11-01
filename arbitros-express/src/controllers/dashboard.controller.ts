import { Request, Response } from 'express'
import * as dashboardService from '../services/dashboard.service'
import { asyncHandler } from '../middleware/error-handler'

export const getDashboard = asyncHandler(async (req: Request, res: Response) => {
	const data = await dashboardService.getDashboard(req, req.params.id)
	res.json(data)
})
