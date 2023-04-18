FROM node:18-alpine

LABEL org.opencontainers.image.source=https://github.com/keyruu/nexcalimat
LABEL org.opencontainers.image.description="Frontend for nexcalimat"
LABEL org.opencontainers.image.licenses=GPL-3.0-only

COPY . .

EXPOSE 3000
CMD ["node", "-r","dotenv/config", "./build"]