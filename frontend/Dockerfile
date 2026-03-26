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
RUN npm install

# Copy everything in the current folder to /app
COPY . .

# Builds the app and creates the /dist folder
RUN npm run build

### BUILDER STAGE END ###




### TESTING STAGE START ###

# TODO: Implement testing stage

### TESTING STAGE END ###




### RUNNER STAGE START ###
# Our runner image, can be changed to something else
# Was used because of its size
FROM busybox:1.37 AS runner

# Same as before in the builder
WORKDIR /app

# Create non-root user
RUN adduser -D appuser

# From builder, copy the dist folder as it is the only
# thing we need for the build
COPY --from=builder /app/dist .

# Switch to non-root user for security
USER appuser

# Command to run the container
CMD ["busybox", "httpd", "-f", "-v", "-p", "8080"]

### RUNNER STAGE END ###