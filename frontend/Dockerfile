# Use a base image with nginx
FROM nginx:alpine

# Copy the build output to replace the default nginx contents
COPY build /usr/share/nginx/html

# Expose the port nginx is running on
EXPOSE 80

# Run nginx
CMD ["nginx", "-g", "daemon off;"]
