package ar.edu.utn.frsf.guardian;

import retrofit2.Call;
import retrofit2.http.GET;

interface ApiService {

    @GET("id")
    Call<String> getId();
}
