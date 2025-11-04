import express from 'express'
import cors from 'cors'
import helmet from 'helmet'
import morgan from 'morgan'
import swaggerUi from 'swagger-ui-express'

import routes from './routes'
import { swaggerSpec } from './config/swagger'
import { errorHandler, notFoundHandler } from './middleware/error-handler'

const app = express()

app.use(helmet())
app.use(cors())
app.use(express.json({ limit: '10mb' }))
app.use(express.urlencoded({ extended: true }))
app.use(morgan('dev'))

app.get('/health', (_req, res) => {
	res.json({ ok: true })
})

app.use('/docs', swaggerUi.serve, swaggerUi.setup(swaggerSpec))
app.use('/api', routes)

app.use(notFoundHandler)
app.use(errorHandler)

export default app
