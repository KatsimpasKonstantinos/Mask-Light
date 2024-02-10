import { Route, Routes } from 'react-router-dom';
import AppShell from './Components/AppShell';
import LandingPage from './Pages/LandingPage';
import Editor from './Pages/Editor/Editor';
import Impressum from './Pages/Utility/Impressum';
import Contact from './Pages/Utility/Contact';
import NotFound from './Pages/Utility/NotFound';

function App() {


  return (
    <AppShell>
      <Routes>
        <Route path='/' element={<LandingPage />} />
        <Route path='/editor' element={<Editor />} />
        <Route path='/impressum' element={<Impressum />} />
        <Route path='/contact' element={<Contact />} />
        <Route path='/*' element={<NotFound />} />
      </Routes>
    </AppShell>
  );
}

export default App;
