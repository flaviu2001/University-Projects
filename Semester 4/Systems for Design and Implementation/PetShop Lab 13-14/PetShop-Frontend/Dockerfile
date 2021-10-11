FROM node:14 
WORKDIR /app
COPY . .
RUN npm install
CMD ["node_modules/@angular/cli/bin/ng", "serve", "--host", "0.0.0.0"]