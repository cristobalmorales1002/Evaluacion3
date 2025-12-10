# Etapa 1: Construcción (Build)
FROM node:18-alpine as build
WORKDIR /app
COPY package.json ./
# Instalamos dependencias
RUN npm install
# Copiamos el resto del código
COPY . .
# Construimos la aplicación para producción (genera carpeta 'build')
RUN npm run build

# Etapa 2: Servidor Web (Nginx)
FROM nginx:alpine
# Copiamos los archivos construidos en la etapa anterior a la carpeta de Nginx
COPY --from=build /app/build /usr/share/nginx/html
# Exponemos el puerto 80 (puerto por defecto de Nginx)
EXPOSE 80
# Arrancamos Nginx
CMD ["nginx", "-g", "daemon off;"]