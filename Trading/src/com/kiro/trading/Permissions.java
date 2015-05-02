package com.kiro.trading;


	public enum Permissions {
		
		TRADE("trade"),
		RELOAD("reload"), 
		SIGN("sign");
		
		private static final String PREFIX = "simpletrading.";
		private String subPermission;
		
		private Permissions(String subPermission) {
			this.subPermission = subPermission;
		}
		
		public String getPermission() {
			return PREFIX + subPermission;
		}
		
}
