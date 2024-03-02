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

A [listener](app/src/main/java/edu/northeastern/a6_group9_artwork_search/stick_it_to_them/DBClientListener.java) will be required. Here is an [example](app/src/androidTest/java/edu/northeastern/a6_group9_artwork_search/RealtimeDatabaseClientTest.java).

### Workflow for message system

#### Init
1. Build your listener.
2. Initiate a [RealtimeDatabaseClient](app/src/main/java/edu/northeastern/a6_group9_artwork_search/stick_it_to_them/RealtimeDatabaseClient.java) instance with your listener. At this time, `listener.onUserAdded` will be triggered multiple times, each time with an existing user in db. This can be used to init friends list.
3. Call `client.loginUser` to login.
4. If login succeeded
   1. `listener.onUserLoggedIn` will be triggered on this device, and a [User](app/src/main/java/edu/northeastern/a6_group9_artwork_search/stick_it_to_them/user/User.java) object will be provided. This can be used to render user info.
   2. `listener.onMessageReceived` will be triggered multiple times, each time with an existing message **sent to or from this user** in db. This can be used to load chat history related with this user.
   3. `listener.onUserAdded` will be triggered once **on the all devices** if it's a **new** user. This can be used to add this user to the friend list.
5. If login failed, `listener.onUserLoggedIn` will be triggered with an error message.

#### Send message
1. Call `client.sendMessage`.
2. `listener.onMessageReceived` will be triggered on **both the sender's and receiver's devices**. This can be used to renew their chat ui.

### Workflow for statistics

#### Counts of stickers
1. Call `client.countStickersSent`.
2. `listener.onCountStickersSentFinished` will be triggered either with the result or the error message.

#### History of stickers received
1. Call `client.retrieveReceivedMessages`.
2. `listener.onRetrieveReceivedMessagesFinished` will be triggered either with the result or the error message.
