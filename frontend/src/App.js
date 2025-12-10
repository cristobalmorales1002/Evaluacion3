import { useState } from 'react';
import { Container } from 'react-bootstrap';
import BarraNavegacion from './components/BarraNavegacion';
import Inventario from './components/Inventario';
import Variantes from './components/Variantes';
import Cotizador from './components/Cotizador'; // <--- IMPORTAR

function App() {
    const [vistaActual, setVistaActual] = useState('inventario');

    const renderizarVista = () => {
        switch (vistaActual) {
            case 'inventario':
                return <Inventario />;
            case 'variantes':
                return <Variantes />;
            case 'cotizador':
                return <Cotizador />; // <--- USAR
            default:
                return <h2>Bienvenido</h2>;
        }
    };

    return (
        <>
            <BarraNavegacion vistaActual={vistaActual} setVistaActual={setVistaActual} />
            <Container>
                {renderizarVista()}
            </Container>
        </>
    );
}

export default App;