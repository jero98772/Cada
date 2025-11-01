import { PutObjectCommand, S3Client } from '@aws-sdk/client-s3'
import { env } from '../config/env'

const region = env.AWS_REGION || 'us-east-1'
const bucket = env.S3_BUCKET

const s3 = new S3Client({
	region,
	credentials:
		env.AWS_ACCESS_KEY_ID && env.AWS_SECRET_ACCESS_KEY
			? {
				accessKeyId: env.AWS_ACCESS_KEY_ID,
				secretAccessKey: env.AWS_SECRET_ACCESS_KEY,
			}
			: undefined,
})

export async function uploadPublic({
	buffer,
	key,
	contentType,
}: {
	buffer: Buffer
	key: string
	contentType?: string
}) {
	if (!bucket) throw new Error('S3_BUCKET is required')

	await s3.send(
		new PutObjectCommand({
			Bucket: bucket,
			Key: key,
			Body: buffer,
			ContentType: contentType,
			ACL: 'public-read',
		}),
	)

	const base = region === 'us-east-1'
		? `https://${bucket}.s3.amazonaws.com`
		: `https://${bucket}.s3.${region}.amazonaws.com`
	return `${base}/${key}`
}
