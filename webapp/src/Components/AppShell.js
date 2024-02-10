import { Link as RouterLink } from 'react-router-dom';
import { ThemeProvider, createTheme, CssBaseline, AppBar, Toolbar, Typography, Drawer, List, ListItem, ListItemIcon, ListItemText, Box, ListItemButton } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';


function AppShell(props) {

    const theme = createTheme({
        palette: {
            primary: {
                main: '#ff0000'
            },
            secondary: {
                main: '#00ff00'
            }
        }
    });

    const drawerWidth = 200;

    function AppShell() {
        return (
            <Box sx={{ display: 'flex' }}>
                <CssBaseline />
                <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
                    <Toolbar>
                        <Typography variant="h6" noWrap component="div">
                            Mask Light
                        </Typography>
                    </Toolbar>
                </AppBar>
                <Drawer
                    variant="permanent"
                    sx={{
                        width: drawerWidth,
                        flexShrink: 0,
                        [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' },
                    }}
                >
                    <Toolbar /> {/* This Toolbar is just for spacing under the AppBar */}
                    <Box sx={{ overflow: 'auto' }}>
                        <List>
                            <ListItem key={"Editor"}>
                                <ListItemButton component={RouterLink} to="/editor">
                                    <ListItemIcon>
                                        <EditIcon />
                                    </ListItemIcon>
                                    <ListItemText primary="Editor" />
                                </ListItemButton>
                            </ListItem>
                        </List>
                    </Box>
                </Drawer>
                <Box
                    component="main"
                    sx={{ flexGrow: 1, bgcolor: 'background.default', p: 3 }}
                >
                    <Toolbar /> {/* This Toolbar is also just for spacing under the AppBar */}
                    {props.children}
                </Box>
            </Box>
        );
    }

    return (
        <div>{AppShell()}</div>
    );
}

export default AppShell;
