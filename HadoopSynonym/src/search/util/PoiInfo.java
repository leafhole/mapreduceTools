package search.util;

public class PoiInfo {

	public PoiInfo() {
		// TODO Auto-generated constructor stub
	}

	private String dataid;
	private String caption;
	private String category;
	private String subcategory;
	private String city;
	
	public String getDataid() {
		return dataid;
	}


	public void setDataid(String dataid) {
		this.dataid = dataid;
	}


	public String getCaption() {
		return caption;
	}


	public void setCaption(String caption) {
		this.caption = caption;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getSubcategory() {
		return subcategory;
	}


	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public float getX() {
		return x;
	}


	public void setX(float x) {
		this.x = x;
	}


	public float getY() {
		return y;
	}


	public void setY(float y) {
		this.y = y;
	}

	private float x;
	private float y;
	
	
	@Override
	public String toString() {
		return "PoiInfo [dataid=" + dataid + ", caption=" + caption
				+ ", category=" + category + ", subcategory=" + subcategory
				+ ", city=" + city + ", x=" + x + ", y=" + y + "]";
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
