import { Router } from 'express'
import * as uploadsController from '../controllers/uploads.controller'
import { requireAuth } from '../middleware/auth'

const router = Router()

router.post('/avatar', requireAuth, uploadsController.uploadAvatar)

export default router
