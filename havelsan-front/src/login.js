import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { useNavigate } from 'react-router-dom';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogActions from '@mui/material/DialogActions';

const defaultTheme = createTheme({
  palette: {
    mode: 'dark',
  },
});

export default function SignIn() {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = React.useState(false);
  const [isErrorDialogOpen, setIsErrorDialogOpen] = React.useState(false);

  const handleSubmit = (event) => {
    event.preventDefault();
    setIsLoading(true);

    const data = new FormData(event.currentTarget);

    fetch('http://localhost:8080/user/login', {
      method: 'POST',
      body: JSON.stringify({
        username: data.get('username'),
        password: data.get('password'),
      }),
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error('Giriş başarısız');
        }
      })
      .then((data) => {
        console.log(data);
        localStorage.setItem('authToken', data.token);
        localStorage.setItem('zoom', 6);
        console.log(localStorage);
        navigate('/map');
      })
      .catch((error) => {
        console.log(error);
        setIsErrorDialogOpen(true);
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  return (
    <ThemeProvider theme={defaultTheme}>
      <Container component='main' maxWidth='xs'>
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component='h1' variant='h5'>
            Sign in
          </Typography>
          <Box
            component='form'
            onSubmit={handleSubmit}
            noValidate
            sx={{ mt: 1 }}
          >
            <TextField
              margin='normal'
              required
              fullWidth
              id='username'
              label='Username'
              name='username'
              autoFocus
            />
            <TextField
              margin='normal'
              required
              fullWidth
              name='password'
              label='Password'
              type='password'
              id='password'
              autoComplete='current-password'
            />
            <Button
              type='submit'
              fullWidth
              variant='contained'
              sx={{ mt: 3, mb: 2 }}
              disabled={isLoading}
            >
              {isLoading ? 'Loading...' : 'Sign In'}
            </Button>
          </Box>
        </Box>
      </Container>
      <Dialog
        open={isErrorDialogOpen}
        onClose={() => setIsErrorDialogOpen(false)}
        aria-labelledby='alert-dialog-title'
        aria-describedby='alert-dialog-description'
      >
        <DialogTitle id='alert-dialog-title'>Giriş Başarısız</DialogTitle>
        <DialogContent>
          <DialogContentText id='alert-dialog-description'>
            Giriş işlemi başarısız oldu. Lütfen kullanıcı adı ve şifrenizi
            kontrol edin.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setIsErrorDialogOpen(false)} color='primary'>
            Tamam
          </Button>
        </DialogActions>
      </Dialog>
    </ThemeProvider>
  );
}
