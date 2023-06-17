FROM nginx:latest

# Copy the static site
# copy everything except .gitignore
COPY . /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]