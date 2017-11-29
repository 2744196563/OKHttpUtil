# 如何引用


```
allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
}
```


```
dependencies {
     compile 'com.github.JiangHaiYang01:OKHttpUtil:1.0.0'
}

```

# get

```androiddatabinding
  OKHttpUtils.create(this)
                .get(urlStr, 1, PhoneBean.class, new OKHttpUtils.OnResponse<PhoneBean>() {
                    @Override
                    public void OnMap(Map<String, String> map) {
                        map.put("phone", "18856907654");
                        map.put("key", "5778e9d9cf089fc3b093b162036fc0e1");
                    }

                    @Override
                    public void onFailure(IOException e) {

                    }

                    @Override
                    public void onResponse(PhoneBean result) {
                        Log.e("TAG", "response---->" + result);
                        textView.setText(result.getResultcode() + " " + result.getResult().getCity());
                    }
                });
```

# post

```androiddatabinding
 OKHttpUtils.create(this)
                .post(urlStr, 1, PhoneBean.class, new OKHttpUtils.OnResponse<PhoneBean>() {
                    @Override
                    public void OnMap(Map<String, String> map) {
                        map.put("phone", "18856907654");
                        map.put("key", "5778e9d9cf089fc3b093b162036fc0e1");
                    }

                    @Override
                    public void onFailure(IOException e) {

                    }

                    @Override
                    public void onResponse(PhoneBean result) {
                        Log.e("TAG", "response---->" + result);
                        textView.setText(result.getResultcode() + " " + result.getResult().getCity());
                    }
                });
```

# downLoad

```androiddatabinding
   OKHttpUtils.create(this)
                .downLoad("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3565619450,1776366346&fm=27&gp=0.jpg",
                        1, "1111",
                        "test.png");
```

# upLoad

```androiddatabinding
  OKHttpUtils.create(this)
                .upLoad("url", 1, "path", "xxx.png", PhoneBean.class, new OKHttpUtils.OnResponse<PhoneBean>() {
                    @Override
                    public void OnMap(Map<String, String> map) {

                    }

                    @Override
                    public void onFailure(IOException e) {

                    }

                    @Override
                    public void onResponse(PhoneBean result) {

                    }
                });
```