# Group9

## At your service

When a `ArtICClient.listxxx` function is called, a [ListResponse](app/src/main/java/edu/northeastern/a6_group9_artwork_search/at_your_service/ListResponse.java) object will be returned, which contains the [pagination](app/src/main/java/edu/northeastern/a6_group9_artwork_search/at_your_service/Pagination.java) information and an array of resources.

Resource may be Artwork or ...

### Artwork

[Field Definition](app/src/main/java/edu/northeastern/a6_group9_artwork_search/at_your_service/Artwork.java)

```java
// fetch an array of artworks
ArtICClient artICClient = new ArtICClient();
ListResponse listArtworkResponse = artICClient.listArtwork(1);
Artwork[] artworks = (Artwork[]) listArtworkResponse.getResources();
```