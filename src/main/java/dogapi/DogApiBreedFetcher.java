package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        // TODO Task 1: Complete this method based on its provided documentation
        //      and the documentation for the dog.ceo API. You may find it helpful
        //      to refer to the examples of using OkHttpClient from the last lab,
        //      as well as the code for parsing JSON responses.
        // return statement included so that the starter code can compile and run.
        DogApiBreedFetcher fetcher = new DogApiBreedFetcher();
        try{
            String response = fetcher.run("https://dog.ceo/api/breed/" + breed.toLowerCase() + "/list");
            JSONObject jsonObject = new JSONObject(response);
            if (!jsonObject.getString("status").equals("success"))
            {
                throw new BreedNotFoundException("Breed: " + breed + " not found");
            }
            JSONArray subBreeds = jsonObject.getJSONArray("message");
            List<String> subBreedsList = new ArrayList<>();
            for (int i = 0; i < subBreeds.length(); i++)
            {
                subBreedsList.add(subBreeds.getString(i));
            }
            return subBreedsList;
        }
        catch (IOException e)
        {
            throw new BreedNotFoundException("Breed: " + breed + " not found");
        }
    }
}