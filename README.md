# whois-finder

This is a very basic application used to identify the location and ownership of a valid IP address using the WhoIs public api.

### running with docker

To run the application in Docker, you simply build the image.

```sh
docker build . -t whoisidentifier
```

Then run the container with the desired IP to be identified as the IP parameter.

```sh
docker run -e IP="8.8.8.8" --rm whoisidentifier
```
