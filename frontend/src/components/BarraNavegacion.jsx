import { Navbar, Container, Nav } from 'react-bootstrap';

// Recibimos 'vistaActual' como prop
const BarraNavegacion = ({ vistaActual, setVistaActual }) => {
    return (
        <Navbar bg="dark" variant="dark" expand="lg" className="mb-4 shadow">
            <Container>
                <Navbar.Brand href="#" onClick={() => setVistaActual('inventario')} style={{cursor: 'pointer'}}>
                    🛋️ Mueblería Los Hermanos
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">

                        <Nav.Link
                            active={vistaActual === 'inventario'}
                            onClick={() => setVistaActual('inventario')}
                            className="fw-bold"
                        >
                            📋 Inventario
                        </Nav.Link>

                        <Nav.Link
                            active={vistaActual === 'variantes'}
                            onClick={() => setVistaActual('variantes')}
                            className="fw-bold"
                        >
                            🎨 Variantes
                        </Nav.Link>

                        <Nav.Link
                            active={vistaActual === 'cotizador'}
                            onClick={() => setVistaActual('cotizador')}
                            className="fw-bold"
                        >
                            💰 Cotizador
                        </Nav.Link>

                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default BarraNavegacion;