import { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Form, Button, Table, ListGroup, Badge, Alert } from 'react-bootstrap';
import { CartPlus, CashCoin, CheckCircle, CartCheck } from 'react-bootstrap-icons';

const Cotizador = () => {
    const BASE_URL = 'http://localhost:8090/api';

    // --- ESTADOS DE DATOS ---
    const [muebles, setMuebles] = useState([]);
    const [variantes, setVariantes] = useState([]);

    // --- ESTADOS DEL FORMULARIO ---
    const [seleccionMueble, setSeleccionMueble] = useState(''); // ID del mueble seleccionado
    const [cantidad, setCantidad] = useState(1);
    const [variantesSeleccionadas, setVariantesSeleccionadas] = useState([]); // Array de IDs

    // --- ESTADOS DEL PROCESO ---
    const [carrito, setCarrito] = useState([]); // Lista de items agregados localmente
    const [cotizacionGenerada, setCotizacionGenerada] = useState(null); // Respuesta del backend
    const [ventaConfirmada, setVentaConfirmada] = useState(false);

    // 1. Cargar Datos Iniciales
    useEffect(() => {
        const cargarDatos = async () => {
            try {
                const resMuebles = await fetch(`${BASE_URL}/muebles`);
                const resVariantes = await fetch(`${BASE_URL}/variantes`);
                setMuebles(await resMuebles.json());
                setVariantes(await resVariantes.json());
            } catch (error) {
                console.error("Error cargando datos:", error);
            }
        };
        cargarDatos();
    }, []);

    // 2. Agregar al Carrito (Local)
    const agregarAlCarrito = () => {
        if (!seleccionMueble) {
            alert("Por favor selecciona un mueble");
            return;
        }

        const muebleObj = muebles.find(m => m.idMueble === parseInt(seleccionMueble));

        // Validar Stock
        if (muebleObj.stock < cantidad) {
            alert(`¡Stock insuficiente! Solo quedan ${muebleObj.stock} unidades.`);
            return;
        }

        // Calcular precio unitario estimado (Visual)
        let precioUnitario = muebleObj.precioBase;
        const nombresVariantes = [];

        variantesSeleccionadas.forEach(idVar => {
            const v = variantes.find(v => v.idVariante === parseInt(idVar));
            if(v) {
                precioUnitario += v.precioAdicional;
                nombresVariantes.push(v.nombre);
            }
        });

        const itemNuevo = {
            idTemp: Date.now(), // ID temporal para la lista visual
            mueble: muebleObj,
            cantidad: parseInt(cantidad),
            variantesIds: variantesSeleccionadas.map(v => parseInt(v)),
            nombresVariantes: nombresVariantes,
            subtotal: precioUnitario * parseInt(cantidad)
        };

        setCarrito([...carrito, itemNuevo]);

        // Resetear formulario parcial
        setSeleccionMueble('');
        setCantidad(1);
        setVariantesSeleccionadas([]);
    };

    // 3. Generar Cotización (Enviar al Backend)
    const generarCotizacion = async () => {
        if (carrito.length === 0) return;

        // Transformar carrito al formato exacto que pide Java (DTO)
        const payload = {
            items: carrito.map(item => ({
                idMueble: item.mueble.idMueble,
                cantidad: item.cantidad,
                idVariantes: item.variantesIds
            }))
        };

        try {
            const response = await fetch(`${BASE_URL}/cotizaciones`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                const data = await response.json();
                setCotizacionGenerada(data); // Guardamos la respuesta del backend (con ID real y Total)
                setCarrito([]); // Limpiamos el carrito visual porque ya está en el backend
            } else {
                alert("Error al generar cotización");
            }
        } catch (error) {
            console.error(error);
            alert("Error de conexión");
        }
    };

    // 4. Confirmar Venta (Finalizar)
    const confirmarVenta = async () => {
        if (!cotizacionGenerada) return;

        try {
            const response = await fetch(`${BASE_URL}/cotizaciones/${cotizacionGenerada.idCotizacion}/confirmar`, {
                method: 'POST'
            });

            if (response.ok) {
                setVentaConfirmada(true);
                // Opcional: Recargar muebles para actualizar stock visualmente si volvemos a cotizar
            } else {
                const errorData = await response.text(); // El backend devuelve texto en caso de error de stock
                alert("Error: " + errorData);
            }
        } catch (error) {
            alert("Error de conexión");
        }
    };

    // Reiniciar todo para nueva venta
    const nuevaVenta = () => {
        setCotizacionGenerada(null);
        setVentaConfirmada(false);
        setCarrito([]);
    };

    // Helper para checkboxes de variantes
    const toggleVariante = (id) => {
        if (variantesSeleccionadas.includes(id)) {
            setVariantesSeleccionadas(variantesSeleccionadas.filter(v => v !== id));
        } else {
            setVariantesSeleccionadas([...variantesSeleccionadas, id]);
        }
    };

    // --- VISTA DE ÉXITO ---
    if (ventaConfirmada) {
        return (
            <Container className="text-center mt-5">
                <div className="p-5 bg-light rounded-3 shadow">
                    <CheckCircle className="text-success display-1 mb-3" />
                    <h1 className="text-success">¡Venta Exitosa!</h1>
                    <p className="lead">El stock ha sido descontado correctamente.</p>
                    <Button variant="primary" size="lg" onClick={nuevaVenta}>
                        Nueva Cotización
                    </Button>
                </div>
            </Container>
        );
    }

    return (
        <Container className="mt-4">
            <Row>
                {/* COLUMNA IZQUIERDA: FORMULARIO */}
                <Col md={5}>
                    <Card className="shadow-sm mb-4">
                        <Card.Header className="bg-primary text-white">
                            <h5 className="mb-0"><CartPlus className="me-2"/>Agregar Producto</h5>
                        </Card.Header>
                        <Card.Body>
                            {/* 1. Seleccionar Mueble */}
                            <Form.Group className="mb-3">
                                <Form.Label>Selecciona Mueble</Form.Label>
                                <Form.Select
                                    value={seleccionMueble}
                                    onChange={(e) => setSeleccionMueble(e.target.value)}
                                    disabled={!!cotizacionGenerada}
                                >
                                    <option value="">-- Seleccionar --</option>
                                    {muebles
                                        .filter(m => m.stock > 0) // Solo mostramos con stock
                                        .map(m => (
                                            <option key={m.idMueble} value={m.idMueble}>
                                                {m.nombreMueble} (${m.precioBase}) - Stock: {m.stock}
                                            </option>
                                        ))}
                                </Form.Select>
                            </Form.Group>

                            {/* 2. Seleccionar Variantes */}
                            <Form.Group className="mb-3">
                                <Form.Label>Variantes (Opcional)</Form.Label>
                                <div className="border p-2 rounded" style={{maxHeight: '150px', overflowY: 'auto'}}>
                                    {variantes.map(v => (
                                        <Form.Check
                                            key={v.idVariante}
                                            type="checkbox"
                                            id={`var-${v.idVariante}`}
                                            label={`${v.nombre} (+$${v.precioAdicional})`}
                                            checked={variantesSeleccionadas.includes(v.idVariante)}
                                            onChange={() => toggleVariante(v.idVariante)}
                                            disabled={!!cotizacionGenerada}
                                        />
                                    ))}
                                </div>
                            </Form.Group>

                            {/* 3. Cantidad */}
                            <Form.Group className="mb-3">
                                <Form.Label>Cantidad</Form.Label>
                                <Form.Control
                                    type="number"
                                    min="1"
                                    value={cantidad}
                                    onChange={(e) => setCantidad(e.target.value)}
                                    disabled={!!cotizacionGenerada}
                                />
                            </Form.Group>

                            <Button
                                variant="outline-primary"
                                className="w-100"
                                onClick={agregarAlCarrito}
                                disabled={!seleccionMueble || !!cotizacionGenerada}
                            >
                                Agregar a la Lista
                            </Button>
                        </Card.Body>
                    </Card>
                </Col>

                {/* COLUMNA DERECHA: RESUMEN Y ACCIONES */}
                <Col md={7}>
                    <Card className="shadow-sm">
                        <Card.Header className={cotizacionGenerada ? "bg-success text-white" : "bg-dark text-white"}>
                            <h5 className="mb-0">
                                {cotizacionGenerada ? `Cotización #${cotizacionGenerada.idCotizacion}` : "🛒 Carrito de Cotización"}
                            </h5>
                        </Card.Header>
                        <Card.Body>

                            {/* Tabla de Items */}
                            {carrito.length === 0 && !cotizacionGenerada ? (
                                <div className="text-center text-muted py-5">
                                    Agrega productos para comenzar
                                </div>
                            ) : (
                                <>
                                    {/* Si ya hay cotización, mostramos los datos del backend, si no, los locales */}
                                    <Table striped bordered size="sm">
                                        <thead>
                                        <tr>
                                            <th>Producto</th>
                                            <th>Variantes</th>
                                            <th>Cant.</th>
                                            <th>Subtotal</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {/* Renderizado condicional: ¿Usamos datos locales o del backend? */}
                                        {(cotizacionGenerada ? cotizacionGenerada.items : carrito).map((item, idx) => (
                                            <tr key={idx}>
                                                <td>{cotizacionGenerada ? item.mueble.nombreMueble : item.mueble.nombreMueble}</td>
                                                <td>
                                                    {cotizacionGenerada
                                                        ? item.variantes.map(v => <Badge key={v.idVariante} bg="info" className="me-1">{v.nombre}</Badge>)
                                                        : item.nombresVariantes.map((n, i) => <Badge key={i} bg="info" className="me-1">{n}</Badge>)
                                                    }
                                                </td>
                                                <td className="text-center">{item.cantidad}</td>
                                                <td className="text-end fw-bold">
                                                    ${(cotizacionGenerada ? item.precioItemFinal : item.subtotal).toLocaleString()}
                                                </td>
                                            </tr>
                                        ))}
                                        </tbody>
                                    </Table>

                                    {/* TOTAL */}
                                    <div className="d-flex justify-content-between align-items-center bg-light p-3 rounded">
                                        <h4 className="mb-0">Total:</h4>
                                        <h3 className="text-primary mb-0">
                                            ${cotizacionGenerada
                                            ? cotizacionGenerada.total.toLocaleString()
                                            : carrito.reduce((acc, item) => acc + item.subtotal, 0).toLocaleString()
                                        }
                                        </h3>
                                    </div>

                                    {/* BOTONES DE ACCIÓN */}
                                    <div className="mt-4 d-grid gap-2">
                                        {!cotizacionGenerada ? (
                                            <Button variant="primary" size="lg" onClick={generarCotizacion}>
                                                <CartCheck className="me-2"/> Generar Cotización (Guardar)
                                            </Button>
                                        ) : (
                                            <>
                                                <Alert variant="warning" className="text-center mb-2">
                                                    La cotización está guardada como <strong>PENDIENTE</strong>.
                                                    <br/>¿Deseas confirmar la venta y descontar stock?
                                                </Alert>
                                                <Button variant="success" size="lg" onClick={confirmarVenta}>
                                                    <CashCoin className="me-2"/> CONFIRMAR VENTA
                                                </Button>
                                                <Button variant="secondary" onClick={nuevaVenta}>
                                                    Cancelar / Nueva
                                                </Button>
                                            </>
                                        )}
                                    </div>
                                </>
                            )}
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default Cotizador;