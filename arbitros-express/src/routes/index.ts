import { Router } from 'express'

import authRoutes from './auth.routes'
import arbitrosRoutes from './arbitros.routes'
import uploadsRoutes from './uploads.routes'
import dashboardRoutes from './dashboard.routes'

const router = Router()

router.get('/', (_req, res) => {
	res.json({ name: 'Arbitros API', version: '1.0.0' })
})

router.use('/auth', authRoutes)
router.use('/arbitros', arbitrosRoutes)
router.use('/uploads', uploadsRoutes)
router.use('/', dashboardRoutes)

export default router
