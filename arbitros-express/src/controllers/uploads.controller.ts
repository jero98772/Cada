import { Request, Response } from 'express'
import multer from 'multer'
import path from 'path'
import { v4 as uuid } from 'uuid'
import { uploadPublic } from '../services/s3.service'
import { asyncHandler } from '../middleware/error-handler'

const upload = multer({ storage: multer.memoryStorage() })

	export const uploadAvatar = [
	upload.single('file'),
	asyncHandler(async (req: Request, res: Response) => {
		const file = req.file as Express.Multer.File | undefined
		if (!file) {
			res.status(400).json({ error: 'file is required' })
			return
		}

		const ext = path.extname(file.originalname) || '.jpg'
		const key = `arbitros/avatars/${uuid()}${ext}`
		const url = await uploadPublic({
			buffer: file.buffer,
			contentType: file.mimetype,
			key,
		})
		res.json({ url })
	}),
]
