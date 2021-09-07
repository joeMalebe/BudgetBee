package za.co.app.budgetbee.ui.report

import za.co.app.budgetbee.data.repository.IDatabaseRepository
import za.co.app.budgetbee.ui.landing.LandingPresenter

class ReportPresenter(transactionsRepository: IDatabaseRepository) : LandingPresenter(transactionsRepository)