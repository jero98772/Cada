import { Request } from 'express'
import * as authService from '../auth.service'
import * as springClient from '../spring-client'

// Mock del spring-client
jest.mock('../spring-client', () => ({
	forward: jest.fn(),
}))

describe('AuthService', () => {
	let mockRequest: Partial<Request>
	const mockForward = springClient.forward as jest.MockedFunction<
		typeof springClient.forward
	>

	beforeEach(() => {
		jest.clearAllMocks()
		mockRequest = {
			body: {},
			params: {},
			headers: {},
		}
	})

	describe('register', () => {
		it('debería llamar a forward con POST /auth/register y el body', async () => {
			const mockBody = {
				username: 'juan.perez',
				email: 'juan@example.com',
				password: 'password123',
				nombre: 'Juan',
				apellido: 'Pérez',
			}
			mockRequest.body = mockBody
			const mockResponse = {
				id: 1,
				username: 'juan.perez',
				email: 'juan@example.com',
			}
			mockForward.mockResolvedValue(mockResponse)

			const result = await authService.register(mockRequest as Request)

			expect(mockForward).toHaveBeenCalledTimes(1)
			expect(mockForward).toHaveBeenCalledWith(
				mockRequest,
				'POST',
				'/auth/register',
				mockBody,
			)
			expect(result).toEqual(mockResponse)
		})

		it('debería propagar errores de validación', async () => {
			const mockBody = { email: 'invalid-email' }
			mockRequest.body = mockBody
			const error = new Error('Validation error')
			mockForward.mockRejectedValue(error)

			await expect(
				authService.register(mockRequest as Request),
			).rejects.toThrow('Validation error')
		})
	})

	describe('login', () => {
		it('debería llamar a forward con POST /auth/login y las credenciales', async () => {
			const mockBody = {
				username: 'juan.perez',
				password: 'password123',
			}
			mockRequest.body = mockBody
			const mockResponse = {
				token: 'jwt-token-123',
				user: {
					id: 1,
					username: 'juan.perez',
				},
			}
			mockForward.mockResolvedValue(mockResponse)

			const result = await authService.login(mockRequest as Request)

			expect(mockForward).toHaveBeenCalledTimes(1)
			expect(mockForward).toHaveBeenCalledWith(
				mockRequest,
				'POST',
				'/auth/login',
				mockBody,
			)
			expect(result).toEqual(mockResponse)
		})

		it('debería manejar credenciales inválidas', async () => {
			const mockBody = {
				username: 'juan.perez',
				password: 'wrong-password',
			}
			mockRequest.body = mockBody
			const error = new Error('Invalid credentials')
			mockForward.mockRejectedValue(error)

			await expect(
				authService.login(mockRequest as Request),
			).rejects.toThrow('Invalid credentials')
		})
	})

	describe('me', () => {
		it('debería llamar a forward con GET /auth/me', async () => {
			mockRequest.headers = {
				authorization: 'Bearer token123',
			}
			const mockResponse = {
				id: 1,
				username: 'juan.perez',
				email: 'juan@example.com',
				nombre: 'Juan',
				apellido: 'Pérez',
			}
			mockForward.mockResolvedValue(mockResponse)

			const result = await authService.me(mockRequest as Request)

			expect(mockForward).toHaveBeenCalledTimes(1)
			expect(mockForward).toHaveBeenCalledWith(
				mockRequest,
				'GET',
				'/auth/me',
			)
			expect(result).toEqual(mockResponse)
		})

		it('debería requerir autenticación', async () => {
			mockRequest.headers = {}
			const error = new Error('Unauthorized')
			mockForward.mockRejectedValue(error)

			await expect(authService.me(mockRequest as Request)).rejects.toThrow(
				'Unauthorized',
			)
		})

		it('debería pasar el header Authorization', async () => {
			mockRequest.headers = {
				authorization: 'Bearer token123',
			}
			mockForward.mockResolvedValue({})

			await authService.me(mockRequest as Request)

			expect(mockForward).toHaveBeenCalledWith(
				expect.objectContaining({
					headers: expect.objectContaining({
						authorization: 'Bearer token123',
					}),
				}),
				'GET',
				'/auth/me',
			)
		})
	})

	describe('manejo de errores de red', () => {
		it('debería propagar errores de conexión', async () => {
			const error = new Error('Network error')
			mockForward.mockRejectedValue(error)

			await expect(
				authService.login(mockRequest as Request),
			).rejects.toThrow('Network error')
		})

		it('debería manejar timeouts', async () => {
			const error = new Error('Request timeout')
			error.name = 'TimeoutError'
			mockForward.mockRejectedValue(error)

			await expect(
				authService.register(mockRequest as Request),
			).rejects.toThrow('Request timeout')
		})
	})

	describe('diferentes formatos de respuesta', () => {
		it('debería manejar respuesta de registro exitoso', async () => {
			mockRequest.body = {
				username: 'test',
				email: 'test@example.com',
				password: 'pass123',
			}
			const mockResponse = {
				success: true,
				message: 'User created successfully',
				user: { id: 1, username: 'test' },
			}
			mockForward.mockResolvedValue(mockResponse)

			const result = await authService.register(mockRequest as Request)

			expect(result).toEqual(mockResponse)
			expect(result).toHaveProperty('success', true)
		})

		it('debería manejar respuesta de login con diferentes estructuras', async () => {
			mockRequest.body = {
				username: 'test',
				password: 'pass123',
			}
			const mockResponse = {
				access_token: 'token123',
				token_type: 'Bearer',
				expires_in: 3600,
			}
			mockForward.mockResolvedValue(mockResponse)

			const result = await authService.login(mockRequest as Request)

			expect(result).toEqual(mockResponse)
			expect(result).toHaveProperty('access_token')
		})
	})
})

