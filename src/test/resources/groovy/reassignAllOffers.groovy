def raterAwardsService = contextProvider.getBean("raterAwardsService")
raterAwardsService.removeAllOffers()
raterAwardsService.reassignAllOffers()