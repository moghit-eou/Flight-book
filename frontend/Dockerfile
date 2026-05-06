FROM node:22-alpine

WORKDIR /app

COPY flight-booking-frontend/package*.json ./

RUN npm install

COPY flight-booking-frontend/ ./

EXPOSE 4200

CMD ["npm", "start", "--", "--host", "0.0.0.0"]