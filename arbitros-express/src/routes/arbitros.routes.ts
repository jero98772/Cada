import { Router } from 'express'
import * as arbitrosController from '../controllers/arbitros.controller'
import { requireAuth } from '../middleware/auth'

const router = Router()

router.get('/', requireAuth, arbitrosController.list)
router.get('/:id', requireAuth, arbitrosController.getById)
router.post('/', requireAuth, arbitrosController.create)
router.put('/:id', requireAuth, arbitrosController.update)
router.get('/:id/partidos', requireAuth, arbitrosController.getPartidos)
router.get('/:id/liquidaciones', requireAuth, arbitrosController.getLiquidaciones)
router.get('/:id/dashboard', requireAuth, arbitrosController.getDashboard)

export default router
