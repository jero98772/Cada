import { Router } from 'express'
import * as dashboardController from '../controllers/dashboard.controller'
import { requireAuth } from '../middleware/auth'

const router = Router()

router.get('/arbitros/:id/dashboard', requireAuth, dashboardController.getDashboard)

export default router
