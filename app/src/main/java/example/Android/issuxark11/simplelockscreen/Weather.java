package example.Android.issuxark11.simplelockscreen;

public class Weather {

    public Location location;
    public CurrentCondition currentCondition = new CurrentCondition();
    public Temperature temperature = new Temperature();

    public byte[] iconData;

    public class CurrentCondition
    {
        private int weatherId;
        private String condition;
        private String weatherdescribe;  // 날씨 상세 설명
        private String icon;

        public int getWeatherId()
        {
            return weatherId;
        }
        public void setWeatherId(int weatherId)
        {
            this.weatherId = weatherId;
        }
        public String getCondition() {
            return condition;
        }
        public void setCondition(String condition) {
            this.condition = condition;
        }
        public String getWeatherdescribe() {
            return weatherdescribe;
        }
        public void setWeatherdescribe(String weatherdescribe) {
            this.weatherdescribe = weatherdescribe;
        }
        public String getIcon() {
            return icon;
        }
        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    public class Temperature
    {
        private float temperature;
        private float minTemperature;
        private float maxTemperature;

        public float getTemperature()
        {
            return temperature;
        }
        public void setTemp(float temp) {
            this.temperature = temp;
        }
        public float getMinTemp() {
            return minTemperature;
        }
        public void setMinTemp(float minTemp) {
            this.minTemperature = minTemp;
        }
        public float getMaxTemp() {
            return maxTemperature;
        }
        public void setMaxTemp(float maxTemp) {
            this.maxTemperature = maxTemp;
        }
    }
}