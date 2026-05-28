### BUILDER STAGE START ###

# The image we will use to build the modules and 
# copy to the actual image
# Using Node 24 as per the requirements
FROM node:24.14-alpine AS builder

# Set the working directory so that we 
# know where the files are generated
WORKDIR /app

# Copy the package list needed for building 
# into the /app directory which we set 
# with the WORKDIR command
COPY package.json package-lock.json ./

# Installing the node modules
RUN npm ci

# Copy everything in the current folder to /app
COPY . .

# Builds the app and creates the /dist folder
RUN npm run build

### BUILDER STAGE END ###




### TESTING STAGE START ###

# TODO: Implement testing stage

### TESTING STAGE END ###




### RUNNER STAGE START ###
FROM nginx:1.27-alpine AS runner

# Copy built assets
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy SPA routing config
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]
### RUNNER STAGE END ###