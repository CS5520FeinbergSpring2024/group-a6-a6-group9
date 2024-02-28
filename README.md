# Group9

## At your service

When a `ArtICClient.listxxx` function is called, a [ListResponse](app/src/main/java/edu/northeastern/a6_group9_artwork_search/at_your_service/ListResponse.java) object will be returned, which contains the [pagination](app/src/main/java/edu/northeastern/a6_group9_artwork_search/at_your_service/Pagination.java) information and an array of resources.

```java
// ListResponse is parcelable, so it can be kept in a bundle
Bundle bundle = new Bundle();
bundle.putParcelable("key", listArtworkResponse);

// extract from a bundle
ListResponse listResponse = bundle.getParcelable("key", ListResponse.class);
```

Resource may be Artwork or Agent

### Artwork

[Field Definition](app/src/main/java/edu/northeastern/a6_group9_artwork_search/at_your_service/Artwork.java)

```java
// fetch an array of artworks
ArtICClient artICClient = new ArtICClient();
ListResponse listArtworkResponse = artICClient.listArtwork(1);
Artwork[] artworks = (Artwork[]) listArtworkResponse.getResources();

// search for artworks which is created before or on 1970
ListResponse listArtworkResponse = artICClient.listArtwork(1, "", "", 0, 1970, "");

// search for artworks which is created after or on 1970, and artist name contains trump, and any metadata contains hey
ListResponse listArtworkResponse = artICClient.listArtwork(1, "hey", "", 1970, 0, "trump");


// get image
Bitmap image = artwork.getImage();
if (image == null) {
    image = artICClient.fetchArtworkImage(artwork);
}
// imageView.setImageBitmap(image)


// get artist based on artwork
Agent artist = artwork.getArtist();
if (artist == null) {
    artist = artICClient.fetchArtworkArtist(artwork);
}

```

### Agent

[Field Definition](app/src/main/java/edu/northeastern/a6_group9_artwork_search/at_your_service/Agent.java)
```java
// fetch an array of agents
ArtICClient artICClient = new ArtICClient();
ListResponse listAgentResponse = artICClient.listAgent(1);
Agent[] agents = (Agent[]) listAgentResponse.getResources();

// search for agents whose id is 98765
ListResponse listAgentResponse = artICClient.listAgent(1, 98765);
```

## Stick it to 'em

A [listener](app/src/main/java/edu/northeastern/a6_group9_artwork_search/stick_it_to_them/DBClientListener.java) will be required. Here is an [example](app/src/androidTest/java/edu/northeastern/a6_group9_artwork_search/RealtimeDatabaseClientTest.java)