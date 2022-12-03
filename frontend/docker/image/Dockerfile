FROM nginx:stable-alpine

COPY docker/image/nginx.conf /etc/nginx/conf.d/default.conf

COPY /build /etc/nginx/html

EXPOSE 80