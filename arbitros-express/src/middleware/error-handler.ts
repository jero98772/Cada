import { NextFunction, Request, Response } from 'express'

export function notFoundHandler(req: Request, res: Response) {
	res.status(404).json({ error: 'Not Found', path: req.originalUrl })
}

export function errorHandler(
	err: any,
	req: Request,
	res: Response,
	_next: NextFunction,
) {
	const status = err?.status || err?.statusCode || 500
	const message = err?.message || 'Internal Server Error'
	res.status(status).json({ error: message })
}

export const asyncHandler =
	(fn: any) => (req: Request, res: Response, next: NextFunction) => {
		Promise.resolve(fn(req, res, next)).catch(next)
	}
