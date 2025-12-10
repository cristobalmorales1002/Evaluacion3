import { useState, useEffect } from 'react';
import { Table, Button, Container, Modal, Form, Badge } from 'react-bootstrap';
import { Trash, PencilSquare, PlusCircle } from 'react-bootstrap-icons';

const Inventario = () => {
    const API_URL = 'http://localhost:8090/api/muebles';

    const [muebles, setMuebles] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [editingId, setEditingId] = useState(null);

    const initialFormState = {
        nombreMueble: '',
        tipo: '',
        precioBase: '',
        stock: '',
        estado: 'ACTIVO', // Se mantiene por defecto interno
        tamaño: 'MEDIANO',
        material: ''
    };

    const [formData, setFormData] = useState(initialFormState);

    const fetchMuebles = async () => {
        try {
            const response = await fetch(API_URL);
            const data = await response.json();
            setMuebles(data);
        } catch (error) {
            console.error("Error:", error);
        }
    };

    useEffect(() => {
        fetchMuebles();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleOpenCreate = () => {
        setEditingId(null);
        setFormData(initialFormState);
        setShowModal(true);
    };

    const handleOpenEdit = (mueble) => {
        setEditingId(mueble.idMueble);
        setFormData({
            nombreMueble: mueble.nombreMueble,
            tipo: mueble.tipo,
            precioBase: mueble.precioBase,
            stock: mueble.stock,
            estado: mueble.estado, // Mantenemos el estado actual del mueble
            tamaño: mueble.tamaño,
            material: mueble.material
        });
        setShowModal(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const method = editingId ? 'PUT' : 'POST';
        const url = editingId ? `${API_URL}/${editingId}` : API_URL;

        try {
            const response = await fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                alert(editingId ? "✅ Mueble actualizado" : "✅ Mueble creado");
                setShowModal(false);
                fetchMuebles();
            } else {
                alert("❌ Error al guardar");
            }
        } catch (error) {
            alert("Error de conexión");
        }
    };

    const handleEliminar = async (id) => {
        if(!window.confirm("¿Seguro que quieres eliminar este mueble?")) return;
        try {
            await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
            fetchMuebles();
        } catch (error) { console.error(error); }
    };

    return (
        <Container className="mt-4">
            <div className="d-flex justify-content-between align-items-center mb-4 p-3 bg-light rounded shadow-sm">
                <h2 className="mb-0 text-primary">📋 Inventario de Muebles</h2>
                <Button variant="success" onClick={handleOpenCreate}>
                    <PlusCircle className="me-2" /> Nuevo Mueble
                </Button>
            </div>

            <div className="table-responsive shadow-sm rounded">
                <Table striped hover className="mb-0">
                    <thead className="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Tipo</th>
                        <th>Tamaño</th>
                        <th>Material</th>
                        <th>Precio</th>
                        <th>Stock</th>
                        <th>Estado</th>
                        <th className="text-center">Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    {muebles.map((m) => (
                        <tr key={m.idMueble}>
                            <td>{m.idMueble}</td>
                            <td>{m.nombreMueble}</td>
                            <td>{m.tipo}</td>
                            <td><Badge bg="info">{m.tamaño}</Badge></td>
                            <td>{m.material}</td>
                            <td>${m.precioBase}</td>
                            <td style={{fontWeight: 'bold', color: m.stock < 5 ? 'red' : 'green'}}>
                                {m.stock}
                            </td>
                            <td>
                                <Badge bg={m.estado === 'ACTIVO' ? 'success' : 'secondary'}>
                                    {m.estado}
                                </Badge>
                            </td>
                            <td className="text-center">
                                <Button variant="warning" size="sm" className="me-2 text-white" onClick={() => handleOpenEdit(m)} title="Editar">
                                    <PencilSquare />
                                </Button>
                                <Button variant="danger" size="sm" onClick={() => handleEliminar(m.idMueble)} title="Eliminar">
                                    <Trash />
                                </Button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            </div>

            {/* MODAL */}
            <Modal show={showModal} onHide={() => setShowModal(false)} backdrop="static">
                <Modal.Header closeButton className={editingId ? "bg-warning" : "bg-primary text-white"}>
                    <Modal.Title>
                        {editingId ? <><PencilSquare className="me-2"/>Editar Mueble</> : <><PlusCircle className="me-2"/>Agregar Nuevo Mueble</>}
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Nombre del Mueble</Form.Label>
                            <Form.Control name="nombreMueble" value={formData.nombreMueble} onChange={handleChange} required />
                        </Form.Group>

                        <div className="row">
                            <div className="col-6">
                                <Form.Group className="mb-3">
                                    <Form.Label>Tipo</Form.Label>
                                    <Form.Control name="tipo" value={formData.tipo} onChange={handleChange} required />
                                </Form.Group>
                            </div>
                            <div className="col-6">
                                <Form.Group className="mb-3">
                                    <Form.Label>Material</Form.Label>
                                    <Form.Control name="material" value={formData.material} onChange={handleChange} required />
                                </Form.Group>
                            </div>
                        </div>

                        <div className="row">
                            <div className="col-6">
                                <Form.Group className="mb-3">
                                    <Form.Label>Precio Base</Form.Label>
                                    <Form.Control type="number" name="precioBase" value={formData.precioBase} onChange={handleChange} required />
                                </Form.Group>
                            </div>
                            <div className="col-6">
                                <Form.Group className="mb-3">
                                    <Form.Label>Stock</Form.Label>
                                    <Form.Control type="number" name="stock" value={formData.stock} onChange={handleChange} required />
                                </Form.Group>
                            </div>
                        </div>

                        {/* SECCIÓN TAMAÑO (Ocupando todo el ancho ahora que quitamos Estado) */}
                        <Form.Group className="mb-3">
                            <Form.Label>Tamaño</Form.Label>
                            <Form.Select name="tamaño" value={formData.tamaño} onChange={handleChange}>
                                <option value="MEDIANO">MEDIANO</option>
                                <option value="GRANDE">GRANDE</option>
                                <option value="PEQUEÑO">PEQUEÑO</option>
                            </Form.Select>
                        </Form.Group>

                        <Button variant={editingId ? "warning" : "primary"} type="submit" className="w-100 mt-2 text-white fw-bold">
                            {editingId ? "Actualizar Cambios" : "Guardar Mueble"}
                        </Button>
                    </Form>
                </Modal.Body>
            </Modal>
        </Container>
    );
};

export default Inventario;