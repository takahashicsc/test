package jp.co.shinoken.repository.impl

import jp.co.shinoken.api.ApiResponse
import jp.co.shinoken.api.Status
import jp.co.shinoken.model.Faq
import jp.co.shinoken.model.FaqCategory
import jp.co.shinoken.model.FaqContent
import jp.co.shinoken.repository.FaqRepository
import javax.inject.Inject

class DataFaqRepository @Inject constructor() : FaqRepository {
    override suspend fun getFaqCategories(): ApiResponse<List<FaqCategory>> {
        return ApiResponse(
            status = Status.SUCCESS,
            data = listOf(
                FaqCategory(1, "共用部"),
                FaqCategory(2, "室内"),
                FaqCategory(3, "契約関連"),
                FaqCategory(4, "家賃"),
                FaqCategory(5, "寒冷地"),
                FaqCategory(6, "各種変更"),
                FaqCategory(7, "駐車場"),
                FaqCategory(8, "生活ルール"),
                FaqCategory(9, "その他"),
            ),
            appError = null
        )
    }

    override suspend fun getFaqContents(): ApiResponse<List<FaqContent>> {
        return ApiResponse(
            status = Status.SUCCESS,
            data = listOf(
                FaqContent(1, "廊下(室外）の共用灯が点灯していません。", 1),
                FaqContent(2, "駐輪場に自転車を置きたいのですが、届け出が必要..", 1),
                FaqContent(3, "メールボックスの暗証番号を教えて欲しい。", 1),
                FaqContent(4, "共用部の植栽に害虫が発生しています。", 1),
                FaqContent(5, "植栽の枝がベランダまで伸び、洗濯物が干せません。", 1),
                FaqContent(6, "駐輪場から自転車が無くなりました。", 1),
                FaqContent(7, "共用部に蜂の巣があります。", 1),
                FaqContent(8, "ガスコンロが点火しません。", 2)
            ),
            appError = null
        )
    }

    override suspend fun getFaqDetail(id: Int): ApiResponse<Faq> {
        val faqList = listOf<Faq>(
            Faq(1, "廊下(室外）の共用灯が点灯していません。", "", false),
            Faq(2, "駐輪場に自転車を置きたいのですが、届け出が必要..", "", false),
            Faq(3, "メールボックスの暗証番号を教えて欲しい。", "", false),
            Faq(4, "共用部の植栽に害虫が発生しています。", "", false),
            Faq(5, "植栽の枝がベランダまで伸び、洗濯物が干せません。", "", false),
            Faq(6, "駐輪場から自転車が無くなりました。", "", false),
            Faq(7, "共用部に蜂の巣があります。", "", false),
            Faq(
                8,
                "ガスコンロが点火しません。",
                "ガスメーターの安全装置がはたらき、ガスの供給が遮断されている可能性があります。他のガス器具（給湯器）の使用が可能かどうかを確認していただき、使用不可な場合は、ガスメーターの復帰操作をお願いします。\nガス遮断が原因ではない場合、電池交換（アルカリ電池）をお願いします。埋め込み型のシステムキッチンの場合、電池挿入口はコンロ下のキャビネット内からコンロの裏側をご確認ください。\n合わせてコンロを真上から見ると、ボールペンの先のような金具（点火プラグ）がございます。その部分を綿棒等で清掃をしてください。\n復旧しない場合は、当社までご連絡をお願いします。",
                true
            ),
        )
        return ApiResponse(
            status = Status.SUCCESS,
            data = faqList.find { it.id == id } ?: faqList[0],
            appError = null
        )

    }
}