# TRAVELERS
## 여행자 간 위치 기반 실시간 거래 메신저 어플리케이션</br></br></br>

TRAVELERS는 여행 중 가까운 사용자들끼리 채팅방에 접속하여 서로 물품을 공유 또는 거래할 수 있는 안드로이드 메신저 어플리케이션입니다.

## 1. 설치 방법 및 사용법

### 1-1. 설치

- .apk(링크) 파일을 안드로이드 기기에 다운로드 후, 실행하여 설치한다. 

### 1-2. 사용법 (+ 스크린샷)

- TRAVELERS 어플리케이션을 접속하여 facebook 아이디로 로그인한다. 
- 본인의 위치 검색을 허용하여 실시간 위치를 탐색한다.
- 본인과 가까운 곳에 있는 사용자들이 생성한 채팅방을 확인한다.
- 관심 있는 채팅방에 접속하거나, 새롭게 채팅방을 만든다.
- 채팅방에서 사용자들과 거래한다.

## 2. 주요 기능

### 2-1. 페이스북 연동 로그인
여행 관련 콘텐츠들을 찾을 때 '페이스북'을 활용하는 경우가 많다고 판단하여 페이스북 아이디를 통해 어플리케이션에 쉽게 로그인 할 수 있도록 하였다. 

#### 2-1-1. 사용자 로그인 여부 체크
```
private CallbackManager callbackManager;
```
FacebookSdk에서 제공하는 CallbackManager 객체는 LoginActivity의 onActivityResult() 메소드를 호출하여 콜백 여부를 판단하고 사용자 데이터를 처리한다.
```
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
}
```

#### 2-1-2. 사용자 로그인 성공 시
```
public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("result",object.toString());
                        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                        startActivity(intent);
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }
```
사용자 로그인이 성공적으로 이루어진 경우 onSuccess() 메소드를 통해 다음 Activity인 MapActivity로 전환된다.


### 2-2. 사용자 실시간 위치 탐색
본 어플의 목적이 여행 중 상품 거래인 만큼, 가까이 있는 사용자와 실시간으로 소통하고 거래를 진행하기 용이하도록 사용자 실시간 위치를 통해 채팅방 목록을 필터링하여 출력한다. 이는 구글 맵 API에서 제공하는 GPS와 Geocoding을 활용하여 사용자 위치를 얻어온 후, 같은 시 또는 도에서 진행하는 거래의 채팅방만 사용자가 볼 수 있도록 하였다.

#### 2-2-1. GPS 사용 퍼미션
```
@Override
public void onMapReady(GoogleMap googleMap) {

    Log.d(TAG, "onMapReady :");

    mGoogleMap = googleMap;


    //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
    //지도의 초기위치를 서울로 이동
    setDefaultLocation();

    //런타임 퍼미션 처리
    // 1. 위치 퍼미션을 가지고 있는지 체크
    int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION);
    int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION);



    if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {

        // 2. 이미 퍼미션을 가지고 있다면
        startLocationUpdates(); // 3. 위치 업데이트 시작


    }else {  // 3. 없다면 퍼미션 요청

        // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

            // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명
            Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                    Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    // 3-3. 사용자에게 퍼미션 요청. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                    ActivityCompat.requestPermissions( MapActivity.this, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE);
                }
            }).show();


        } else {
            // 3-4. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청
            // 요청 결과는 onRequestPermissionResult에서 수신
            ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE);
        }

    }

    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

        @Override
        public void onMapClick(LatLng latLng) {

            Log.d( TAG, "onMapClick :");
        }
    });
}
```
onMapReady() 메소드에서는 사용자로부터 GPS 위치 탐색 퍼미션을 받는다. 사용자가 본 어플에서 위치 정보 사용을 허용한 경우 위치를 탐색하는 getCurrentAddress() 메소드를 호출할 수 있다.

#### 2-2-2. 사용자 현위치 탐색 및 저장
```
public String getCurrentAddress(LatLng latlng) {

    Geocoder geocoder = new Geocoder(this, Locale.getDefault());

    List<Address> addresses;

    try {

        addresses = geocoder.getFromLocation(
                latlng.latitude,
                latlng.longitude,
                1);
    } catch (IOException ioException) {
        // 네트워크 문제
        Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
        return "지오코더 서비스 사용불가";
    } catch (IllegalArgumentException illegalArgumentException) {
        Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
        return "잘못된 GPS 좌표";

    }


    if (addresses == null || addresses.size() == 0) {
        Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
        return "주소 미발견";

    } else {
        Address address = addresses.get(0);
        myLocation = address.getAddressLine(0).toString();
        return myLocation;
    }

}
```
getCurrentAddress() 메소드에서는 Geocoder 객체를 생성하여 좌표를 주소로 변환한다. latlng 객체에서 경도와 위도를 인자로 전달받아 addresses에 주소를 저장한 후 반환한다.
```
if (locationList.size() > 0) {
    location = locationList.get(locationList.size() - 1);
    //location = locationList.get(0);

    currentPosition
            = new LatLng(location.getLatitude(), location.getLongitude());


    markerTitle = getCurrentAddress(currentPosition);
    markerTitle = markerTitle.replace("대한민국","");

    TextView address_view = (TextView) findViewById(R.id.address) ;
    address_view.setText(String.format(markerTitle));

    String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
            + " 경도:" + String.valueOf(location.getLongitude());

    Log.d(TAG, "onLocationResult : " + markerSnippet);


    // 현재 위치에 마커 생성하고 이동
    setCurrentLocation(location, markerTitle, markerSnippet);

    mCurrentLocatiion = location;
}
```
onCreate() 메소드 안에서 반환받은 addresses를 인자로 넘겨 지도 위에 사용자 위치를 출력하는 setCurrentLocation() 메소드를 호출한다.

#### 2-2-3. 사용자 현위치 지도 위 출력
```
public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {

    if (currentMarker != null) currentMarker.remove();

    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

    MarkerOptions markerOptions = new MarkerOptions();
    markerOptions.position(currentLatLng);
    markerOptions.title(markerTitle);
    markerOptions.snippet(markerSnippet);
    markerOptions.draggable(true);


    currentMarker = mGoogleMap.addMarker(markerOptions);

    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
    mGoogleMap.moveCamera(cameraUpdate);

}
```
setCurrentLocation() 메소드는 사용자의 현 위치를 지도 위의 Marker로 출력한다.

#### 2-2-4. MainActivity 호출
```
Button yesButton = (Button)findViewById(R.id.yesB);

    yesButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent_main = new Intent(MapActivity.this, MainActivity.class);
            intent_main.putExtra("user_addr",markerTitle);
            startActivity(intent_main);
        }
    });
```
사용자가 자신의 현 위치를 확인한 후 YES Button을 클릭하면 다음 Activity인 MainActivity로 전환하며, 이 때 사용자의 주소를 통해 채팅방 목록을 Filtering 하기 위해 user_addr을 추가 인자로 넘겨준다.


### 2-3. 채팅방 목록 출력
Cloud Firestore에서 전체 채팅방 목록을 불러와 MainActivity 화면의 AllChatFragment에 출력한다. 이 때 나타나는 채팅방 목록은 위의 메소드에서 전달받은 사용자 현 위치를 활용하여 자동으로 필터링된 결과이다.

#### 2-3-1. 채팅방 목록 Fragment


#### 2-3-2. 채팅방 목록 필터링


### 2-4. 새로운 채팅방 개설 
MainActivity에서 새로운 채팅방 개설을 위한 FAB를 클릭한 경우 CreateRoomActivity로 전환된다.

```
FirebaseFirestore db = FirebaseFirestore.getInstance();
```
채팅방 목록이 저장되어 있는 Firestore에 접근하기 위해 FirebaseFirestore 객체를 생성한다.
```
public void onClick(View v){
    if(v == deadlineView){
            Calendar c= Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener(){
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                            System.out.println(year);
                            System.out.println(monthOfYear);
                            System.out.println(dayOfMonth);
                        }
                    }, year, month, day);
            datePickerDialog.show();

    }
    else if(v == createBtn) {
        String title = titleView.getText().toString();
        String remains = remainsView.getText().toString();
        String content = contentView.getText().toString();

        Intent intent = getIntent();
        intent.putExtra("title",title);
        intent.putExtra("remains",remains);
        intent.putExtra("location",location);
        ChatRoom room = new ChatRoom(title, "ME", remains, "20181213", content, location);
        db.collection("chatrooms").add(room);
        setResult(RESULT_OK,intent);
        finish();
    }
}
```
화면의 필드로부터 사용자가 입력한 값을 가져와 새로운 ChatRoom 객체를 생성하고, 이를 Firestore에 채팅방으로 추가한다. 이 때 채팅방 이름, 잔여 수량, 상세 정보는 텍스트 필드에서 입력을 받아 오고, 유효기간은 Calander 객체를 활용하여 입력받는다. 또한, 물건 거래 위치는 Spinner로 정해진 옵션으로만 입력을 받아 위치 기반 필터링에 용이하도록 하였다.


### 2-5. 채팅방 내 거래




## 3. 사용 오픈 소스 
- [Firebase의 Realtime Database를 이용한 채팅방 App](https://github.com/peanutsando/FirebaseChatRoom)
- [페이스북 로그인 API](https://developers.facebook.com/docs/facebook-login/)
- [구글 지도 API](https://developers.google.com/maps/documentation/?hl=ko)

## 4. 개발자 정보

- 1315024 김지혜(jae57) : 데이터베이스 연동 및 채팅방 목록 시각화, 최종발표자
- 1515047 이승연 (ssyylee729) : 페이스북 연동 로그인 화면 제작, Google Map API 이용한 사용자 위치 검색, 중간발표자
- 1717009 김지우 (bxrlxy) : 위치 기반 채팅방 필터링, README.md 작성, 최종발표자
- 1717056 허선(sunpaka1018) : 사용자 인터페이스 디자인, 중간발표자

## 5. 라이센스

* [Apache 2.0 License](https://github.com/jae57/TRAVELERS_androidMessenger/blob/master/LICENSE)

