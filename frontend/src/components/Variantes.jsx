import { useState, useEffect } from 'react';
import { Table, Button, Container, Modal, Form, Badge, InputGroup } from 'react-bootstrap';
import { Trash, PlusCircle, Tag } from 'react-bootstrap-icons';

const Variantes = () => {
    const API_URL = 'http://localhost:8090/api/variantes';

    const [variantes, setVariantes] = useState([]);
    const [showModal, setShowModal] = useState(false);

    // Estado del formulario
    const [formData, setFormData] = useState({
        nombre: '',
        precioAdicional: ''
    });

    // 1. Cargar Variantes
    const fetchVariantes = async () => {
        try {
            const response = await fetch(API_URL);
            if (response.ok) {
                const data = await response.json();
                setVariantes(data);
            } else {
                console.error("Error al cargar variantes");
            }
        } catch (error) {
            console.error("Error de conexión:", error);
        }
    };

    useEffect(() => {
        fetchVariantes();
    }, []);

    // 2. Manejar Inputs
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    // 3. Crear Variante
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(API_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                alert("✅ Variante registrada correctamente");
                setShowModal(false);
                setFormData({ nombre: '', precioAdicional: '' }); // Limpiar
                fetchVariantes(); // Recargar tabla
            } else {
                alert("❌ Error al guardar. Verifica que el nombre no esté repetido.");
            }
        } catch (error) {
            alert("Error de conexión con el servidor");
        }
    };

    // 4. Eliminar Variante
    const handleEliminar = async (id) => {
        if(!window.confirm("¿Eliminar esta variante? Si la borras, podría afectar cotizaciones antiguas.")) return;

        try {
            const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
            if (response.ok) {
                fetchVariantes();
            } else {
                alert("No se pudo eliminar");
            }
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <Container className="mt-4">
            {/* Encabezado */}
            <div className="d-flex justify-content-between align-items-center mb-4 p-3 bg-light rounded shadow-sm">
                <h2 className="mb-0 text-primary">🎨 Gestión de Variantes</h2>
                <Button variant="success" onClick={() => setShowModal(true)}>
                    <PlusCircle className="me-2" /> Nueva Variante
                </Button>
            </div>

            <div className="table-responsive shadow-sm rounded">
                <Table striped hover className="mb-0 align-middle">
                    <thead className="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Descripción / Nombre</th>
                        <th>Precio Extra</th>
                        <th className="text-center">Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    {variantes.length === 0 ? (
                        <tr>
                            <td colSpan="4" className="text-center text-muted">No hay variantes registradas</td>
                        </tr>
                    ) : (
                        variantes.map((v) => (
                            <tr key={v.idVariante}>
                                <td><Badge bg="secondary">#{v.idVariante}</Badge></td>
                                <td className="fw-bold">{v.nombre}</td>
                                <td className="text-success fw-bold">
                                    +${v.precioAdicional.toLocaleString()}
                                </td>
                                <td className="text-center">
                                    <Button
                                        variant="danger"
                                        size="sm"
                                        onClick={() => handleEliminar(v.idVariante)}
                                        title="Eliminar"
                                    >
                                        <Trash />
                                    </Button>
                                </td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </Table>
            </div>

            {/* MODAL DE CREACIÓN */}
            <Modal show={showModal} onHide={() => setShowModal(false)} centered>
                <Modal.Header closeButton className="bg-success text-white">
                    <Modal.Title><Tag className="me-2"/>Registrar Variante</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Nombre de la Variante</Form.Label>
                            <Form.Control
                                name="nombre"
                                placeholder="Ej: Barniz Premium, Ruedas, Cojines..."
                                value={formData.nombre}
                                onChange={handleChange}
                                required
                                autoFocus
                            />
                        </Form.Group>

                        <Form.Group className="mb-4">
                            <Form.Label>Precio Adicional</Form.Label>
                            <InputGroup>
                                <InputGroup.Text>$</InputGroup.Text>
                                <Form.Control
                                    type="number"
                                    name="precioAdicional"
                                    placeholder="0"
                                    value={formData.precioAdicional}
                                    onChange={handleChange}
                                    required
                                    min="0"
                                />
                            </InputGroup>
                            <Form.Text className="text-muted">
                                Este valor se sumará al precio base del mueble en la cotización.
                            </Form.Text>
                        </Form.Group>

                        <Button variant="success" type="submit" className="w-100 fw-bold">
                            Guardar Variante
                        </Button>
                    </Form>
                </Modal.Body>
            </Modal>
        </Container>
    );
};

export default Variantes;