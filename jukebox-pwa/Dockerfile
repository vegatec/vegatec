# Dockerfile

FROM node:10.12-slim as builder

ADD . /app
WORKDIR /app
RUN npm install
RUN npm run build -prod

ENV NODE_ENV=production
WORKDIR /app/express
RUN npm install
RUN wget https://gobinaries.com/tj/node-prune --output-document - | /bin/sh && node-prune


FROM gcr.io/distroless/nodejs18-debian11

COPY --from=builder /app/build/resources/main/static /app/angular/build
COPY --from=builder /app/express/node_modules /app/express/node_modules
COPY --from=builder /app/express/index.js /app/express/index.js

WORKDIR /app/express
EXPOSE 3000
CMD ["index.js"]