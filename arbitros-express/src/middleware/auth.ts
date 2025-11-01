import { Request, Response, NextFunction } from 'express'

export function requireAuth(req: Request, res: Response, next: NextFunction) {
	const auth = req.headers['authorization']
	if (!auth || typeof auth !== 'string' || !auth.startsWith('Bearer ')) {
		res.status(401).json({ error: 'Unauthorized' })
		return
	}
	next()
}
