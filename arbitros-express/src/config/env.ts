import dotenv from 'dotenv'
import { z } from 'zod'

dotenv.config()

const envSchema = z.object({
	PORT: z.coerce.number().default(3000),
	SPRING_API_BASE_URL: z
		.string({ required_error: 'SPRING_API_BASE_URL is required' })
		.url('SPRING_API_BASE_URL must be a URL'),
	AWS_REGION: z.string().optional(),
	AWS_ACCESS_KEY_ID: z.string().optional(),
	AWS_SECRET_ACCESS_KEY: z.string().optional(),
	S3_BUCKET: z.string().optional(),
})

export const env = envSchema.parse(process.env)
