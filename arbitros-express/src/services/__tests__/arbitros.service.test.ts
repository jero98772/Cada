import { Request } from 'express'
import * as arbitrosService from '../arbitros.service'
import * as springClient from '../spring-client'

// Mock del spring-client
jest.mock('../spring-client', () => ({
	forward: jest.fn(),
}))

describe('ArbitroService', () => {
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

	describe('list', () => {
		it('debería llamar a forward con GET /api/v1/arbitros', async () => {
			const mockData = [{ id: 1, nombre: 'Juan' }]
			mockForward.mockResolvedValue(mockData)

			const result = await arbitrosService.list(mockRequest as Request)

			expect(mockForward).toHaveBeenCalledTimes(1)
			expect(mockForward).toHaveBeenCalledWith(
				mockRequest,
				'GET',
				'/api/v1/arbitros',
			)
			expect(result).toEqual(mockData)
		})
	})

	describe('getById', () => {
		it('debería llamar a forward con GET /api/v1/arbitros/:id', async () => {
			const id = '123'
			const mockData = { id: 123, nombre: 'Juan', apellido: 'Pérez' }
			mockForward.mockResolvedValue(mockData)

			const result = await arbitrosService.getById(mockRequest as Request, id)

			expect(mockForward).toHaveBeenCalledTimes(1)
			expect(mockForward).toHaveBeenCalledWith(
				mockRequest,
				'GET',
				'/api/v1/arbitros/123',
			)
			expect(result).toEqual(mockData)
		})
	})

	describe('create', () => {
		it('debería llamar a forward con POST /api/v1/arbitros y el body', async () => {
			const mockBody = {
				nombre: 'Juan',
				apellido: 'Pérez',
				email: 'juan@example.com',
			}
			mockRequest.body = mockBody
			const mockData = { id: 1, ...mockBody }
			mockForward.mockResolvedValue(mockData)

			const result = await arbitrosService.create(mockRequest as Request)

			expect(mockForward).toHaveBeenCalledTimes(1)
			expect(mockForward).toHaveBeenCalledWith(
				mockRequest,
				'POST',
				'/api/v1/arbitros',
				mockBody,
			)
			expect(result).toEqual(mockData)
		})
	})

	describe('update', () => {
		it('debería llamar a forward con PUT /api/v1/arbitros/:id y el body', async () => {
			const id = '123'
			const mockBody = {
				nombre: 'Juan Carlos',
				apellido: 'Pérez',
			}
			mockRequest.body = mockBody
			const mockData = { id: 123, ...mockBody }
			mockForward.mockResolvedValue(mockData)

			const result = await arbitrosService.update(mockRequest as Request, id)

			expect(mockForward).toHaveBeenCalledTimes(1)
			expect(mockForward).toHaveBeenCalledWith(
				mockRequest,
				'PUT',
				'/api/v1/arbitros/123',
				mockBody,
			)
			expect(result).toEqual(mockData)
		})
	})

	describe('getPartidos', () => {
		it('debería llamar a forward con GET /arbitros/:id/partidos', async () => {
			const id = '123'
			const mockData = [
				{ id: 1, fecha: '2024-01-01', equipoLocal: 'Equipo A' },
			]
			mockForward.mockResolvedValue(mockData)

			const result = await arbitrosService.getPartidos(mockRequest as Request, id)

			expect(mockForward).toHaveBeenCalledTimes(1)
			expect(mockForward).toHaveBeenCalledWith(
				mockRequest,
				'GET',
				'/arbitros/123/partidos',
			)
			expect(result).toEqual(mockData)
		})
	})

	describe('getLiquidaciones', () => {
		it('debería llamar a forward con GET /arbitros/:id/liquidaciones', async () => {
			const id = '123'
			const mockData = [{ id: 1, monto: 1000, fecha: '2024-01-01' }]
			mockForward.mockResolvedValue(mockData)

			const result = await arbitrosService.getLiquidaciones(
				mockRequest as Request,
				id,
			)

			expect(mockForward).toHaveBeenCalledTimes(1)
			expect(mockForward).toHaveBeenCalledWith(
				mockRequest,
				'GET',
				'/arbitros/123/liquidaciones',
			)
			expect(result).toEqual(mockData)
		})
	})

	describe('getDashboard', () => {
		it('debería llamar a forward con GET /arbitros/:id/dashboard', async () => {
			const id = '123'
			const mockData = {
				arbitro: { id: 123, nombre: 'Juan' },
				partidosPendientes: 5,
				partidosAceptados: 10,
			}
			mockForward.mockResolvedValue(mockData)

			const result = await arbitrosService.getDashboard(mockRequest as Request, id)

			expect(mockForward).toHaveBeenCalledTimes(1)
			expect(mockForward).toHaveBeenCalledWith(
				mockRequest,
				'GET',
				'/arbitros/123/dashboard',
			)
			expect(result).toEqual(mockData)
		})
	})

	describe('manejo de errores', () => {
		it('debería propagar errores de forward', async () => {
			const error = new Error('Network error')
			mockForward.mockRejectedValue(error)

			await expect(
				arbitrosService.list(mockRequest as Request),
			).rejects.toThrow('Network error')
		})
	})

	describe('headers de autorización', () => {
		it('debería pasar el header Authorization cuando existe', async () => {
			mockRequest.headers = {
				authorization: 'Bearer token123',
			}
			mockForward.mockResolvedValue([])

			await arbitrosService.list(mockRequest as Request)

			expect(mockForward).toHaveBeenCalledWith(
				expect.objectContaining({
					headers: expect.objectContaining({
						authorization: 'Bearer token123',
					}),
				}),
				'GET',
				'/api/v1/arbitros',
			)
		})
	})
})

