import { render, screen } from "../customRender";
import ChartMfd from "./ChartMfd";
import Chart from "react-apexcharts";

const dataMfd = {
    year: 2022,
    month: 4,
    day: 5,
    dayOfWeek: 0,
    cameraId: null,
    intersectionId: null,
    regionId: null,
    data: [
        { hour: 0, min: 0, srlu: 2514, qtsrlu: 13818 },
        { hour: 0, min: 15, srlu: 2330, qtsrlu: 14115 },
        { hour: 0, min: 30, srlu: 2238, qtsrlu: 10798 },
        { hour: 0, min: 45, srlu: 1748, qtsrlu: 8571 },
        { hour: 1, min: 0, srlu: 1564, qtsrlu: 8043 },
        { hour: 1, min: 15, srlu: 1409, qtsrlu: 7561 },
        { hour: 1, min: 30, srlu: 1157, qtsrlu: 6276 },
        { hour: 1, min: 45, srlu: 987, qtsrlu: 6079 },
        { hour: 2, min: 0, srlu: 1072, qtsrlu: 6000 },
        { hour: 2, min: 15, srlu: 874, qtsrlu: 5628 },
        { hour: 2, min: 30, srlu: 738, qtsrlu: 5350 },
        { hour: 2, min: 45, srlu: 632, qtsrlu: 4676 },
        { hour: 3, min: 0, srlu: 692, qtsrlu: 5074 },
        { hour: 3, min: 15, srlu: 713, qtsrlu: 4909 },
        { hour: 3, min: 30, srlu: 748, qtsrlu: 5126 },
        { hour: 3, min: 45, srlu: 713, qtsrlu: 5711 },
        { hour: 4, min: 0, srlu: 790, qtsrlu: 5919 },
        { hour: 4, min: 15, srlu: 774, qtsrlu: 5504 },
        { hour: 4, min: 30, srlu: 672, qtsrlu: 3219 },
        { hour: 4, min: 45, srlu: 259, qtsrlu: 1247 },
        { hour: 5, min: 0, srlu: 26, qtsrlu: 100 },
        { hour: 11, min: 15, srlu: 3, qtsrlu: 24 },
        { hour: 11, min: 30, srlu: 282, qtsrlu: 4766 },
        { hour: 11, min: 45, srlu: 4330, qtsrlu: 38228 },
        { hour: 12, min: 0, srlu: 9052, qtsrlu: 82034 },
        { hour: 12, min: 15, srlu: 10764, qtsrlu: 94518 },
        { hour: 12, min: 30, srlu: 10455, qtsrlu: 91709 },
        { hour: 12, min: 45, srlu: 10328, qtsrlu: 91984 },
        { hour: 13, min: 0, srlu: 11035, qtsrlu: 103106 },
        { hour: 13, min: 15, srlu: 11371, qtsrlu: 101678 },
        { hour: 13, min: 30, srlu: 11827, qtsrlu: 106379 },
        { hour: 13, min: 45, srlu: 11905, qtsrlu: 107057 },
        { hour: 14, min: 0, srlu: 11718, qtsrlu: 103459 },
        { hour: 14, min: 15, srlu: 8989, qtsrlu: 81876 },
    ],
};

const dataLastWeekMfd = {
    year: 2022,
    month: 3,
    day: 29,
    dayOfWeek: 0,
    cameraId: null,
    intersectionId: null,
    regionId: null,
    data: [
        { hour: 0, min: 0, srlu: 2246, qtsrlu: 16772 },
        { hour: 0, min: 15, srlu: 1949, qtsrlu: 13703 },
        { hour: 0, min: 30, srlu: 1542, qtsrlu: 10366 },
        { hour: 0, min: 45, srlu: 1472, qtsrlu: 11169 },
        { hour: 1, min: 0, srlu: 1314, qtsrlu: 10399 },
        { hour: 1, min: 15, srlu: 1106, qtsrlu: 9346 },
        { hour: 1, min: 30, srlu: 920, qtsrlu: 7540 },
        { hour: 1, min: 45, srlu: 869, qtsrlu: 7690 },
        { hour: 2, min: 0, srlu: 834, qtsrlu: 6458 },
        { hour: 2, min: 15, srlu: 761, qtsrlu: 6525 },
        { hour: 2, min: 30, srlu: 677, qtsrlu: 5702 },
        { hour: 2, min: 45, srlu: 697, qtsrlu: 6427 },
        { hour: 3, min: 0, srlu: 841, qtsrlu: 6628 },
        { hour: 3, min: 15, srlu: 844, qtsrlu: 6085 },
        { hour: 3, min: 30, srlu: 661, qtsrlu: 6354 },
        { hour: 3, min: 45, srlu: 626, qtsrlu: 5874 },
        { hour: 4, min: 0, srlu: 779, qtsrlu: 6670 },
        { hour: 4, min: 15, srlu: 855, qtsrlu: 7888 },
        { hour: 4, min: 30, srlu: 1112, qtsrlu: 10204 },
        { hour: 4, min: 45, srlu: 1343, qtsrlu: 9559 },
        { hour: 5, min: 0, srlu: 1779, qtsrlu: 13921 },
        { hour: 5, min: 15, srlu: 2477, qtsrlu: 19023 },
        { hour: 5, min: 30, srlu: 3132, qtsrlu: 22318 },
        { hour: 5, min: 45, srlu: 4062, qtsrlu: 29602 },
        { hour: 6, min: 0, srlu: 5039, qtsrlu: 42200 },
        { hour: 6, min: 15, srlu: 6037, qtsrlu: 53698 },
        { hour: 6, min: 30, srlu: 6429, qtsrlu: 58370 },
        { hour: 6, min: 45, srlu: 7401, qtsrlu: 59384 },
        { hour: 7, min: 0, srlu: 8091, qtsrlu: 64241 },
        { hour: 7, min: 15, srlu: 9538, qtsrlu: 74798 },
        { hour: 7, min: 30, srlu: 11452, qtsrlu: 88784 },
        { hour: 7, min: 45, srlu: 13324, qtsrlu: 100130 },
        { hour: 8, min: 0, srlu: 14346, qtsrlu: 116819 },
        { hour: 8, min: 15, srlu: 15605, qtsrlu: 121165 },
        { hour: 8, min: 30, srlu: 15243, qtsrlu: 123847 },
        { hour: 8, min: 45, srlu: 13244, qtsrlu: 103385 },
        { hour: 9, min: 0, srlu: 12082, qtsrlu: 90949 },
        { hour: 9, min: 15, srlu: 11697, qtsrlu: 91394 },
        { hour: 9, min: 30, srlu: 12116, qtsrlu: 92729 },
        { hour: 9, min: 45, srlu: 12116, qtsrlu: 95681 },
        { hour: 10, min: 0, srlu: 11217, qtsrlu: 95692 },
        { hour: 10, min: 15, srlu: 11723, qtsrlu: 98148 },
        { hour: 10, min: 30, srlu: 11989, qtsrlu: 100963 },
        { hour: 10, min: 45, srlu: 11444, qtsrlu: 101701 },
        { hour: 11, min: 0, srlu: 11372, qtsrlu: 101541 },
        { hour: 11, min: 15, srlu: 11387, qtsrlu: 95418 },
        { hour: 11, min: 30, srlu: 11310, qtsrlu: 98806 },
        { hour: 11, min: 45, srlu: 11221, qtsrlu: 98726 },
        { hour: 12, min: 0, srlu: 10587, qtsrlu: 96762 },
        { hour: 12, min: 15, srlu: 10132, qtsrlu: 89476 },
        { hour: 12, min: 30, srlu: 10529, qtsrlu: 90901 },
        { hour: 12, min: 45, srlu: 10251, qtsrlu: 91366 },
        { hour: 13, min: 0, srlu: 10614, qtsrlu: 89770 },
        { hour: 13, min: 15, srlu: 10749, qtsrlu: 93377 },
        { hour: 13, min: 30, srlu: 11164, qtsrlu: 97641 },
        { hour: 13, min: 45, srlu: 10854, qtsrlu: 93417 },
        { hour: 14, min: 0, srlu: 11292, qtsrlu: 97256 },
        { hour: 14, min: 15, srlu: 11407, qtsrlu: 97812 },
        { hour: 14, min: 30, srlu: 11615, qtsrlu: 102294 },
        { hour: 14, min: 45, srlu: 11629, qtsrlu: 100892 },
        { hour: 15, min: 0, srlu: 11246, qtsrlu: 98339 },
        { hour: 15, min: 15, srlu: 11436, qtsrlu: 97619 },
        { hour: 15, min: 30, srlu: 11672, qtsrlu: 99453 },
        { hour: 15, min: 45, srlu: 11548, qtsrlu: 97364 },
        { hour: 16, min: 0, srlu: 11366, qtsrlu: 102618 },
        { hour: 16, min: 15, srlu: 12450, qtsrlu: 104992 },
        { hour: 16, min: 30, srlu: 12369, qtsrlu: 109987 },
        { hour: 16, min: 45, srlu: 12881, qtsrlu: 108389 },
        { hour: 17, min: 0, srlu: 13481, qtsrlu: 117954 },
        { hour: 17, min: 15, srlu: 13504, qtsrlu: 120409 },
        { hour: 17, min: 30, srlu: 13633, qtsrlu: 121230 },
        { hour: 17, min: 45, srlu: 13441, qtsrlu: 127136 },
        { hour: 18, min: 0, srlu: 14300, qtsrlu: 142094 },
        { hour: 18, min: 15, srlu: 14759, qtsrlu: 154007 },
        { hour: 18, min: 30, srlu: 13914, qtsrlu: 144622 },
        { hour: 18, min: 45, srlu: 12154, qtsrlu: 116104 },
        { hour: 19, min: 0, srlu: 11288, qtsrlu: 98130 },
        { hour: 19, min: 15, srlu: 9620, qtsrlu: 79711 },
        { hour: 19, min: 30, srlu: 8442, qtsrlu: 65965 },
        { hour: 19, min: 45, srlu: 7656, qtsrlu: 60090 },
        { hour: 20, min: 0, srlu: 7200, qtsrlu: 55149 },
        { hour: 20, min: 15, srlu: 7022, qtsrlu: 58928 },
        { hour: 20, min: 30, srlu: 6836, qtsrlu: 54019 },
        { hour: 20, min: 45, srlu: 6167, qtsrlu: 48656 },
        { hour: 21, min: 0, srlu: 6442, qtsrlu: 52586 },
        { hour: 21, min: 15, srlu: 6581, qtsrlu: 51675 },
        { hour: 21, min: 30, srlu: 5984, qtsrlu: 46189 },
        { hour: 21, min: 45, srlu: 5616, qtsrlu: 41572 },
        { hour: 22, min: 0, srlu: 5290, qtsrlu: 39280 },
        { hour: 22, min: 15, srlu: 5201, qtsrlu: 40738 },
        { hour: 22, min: 30, srlu: 4206, qtsrlu: 31269 },
        { hour: 22, min: 45, srlu: 3891, qtsrlu: 26185 },
        { hour: 23, min: 0, srlu: 3772, qtsrlu: 29276 },
        { hour: 23, min: 15, srlu: 3584, qtsrlu: 25584 },
        { hour: 23, min: 30, srlu: 3030, qtsrlu: 20565 },
        { hour: 23, min: 45, srlu: 2488, qtsrlu: 16534 },
    ],
};

const dataLastMonthAvgMfd = {
    year: 0,
    month: 0,
    day: 0,
    dayOfWeek: 2,
    cameraId: null,
    intersectionId: null,
    regionId: null,
    data: [
        { hour: 0, min: 0, srlu: 8588, qtsrlu: 55795 },
        { hour: 0, min: 15, srlu: 7549, qtsrlu: 47873 },
        { hour: 0, min: 30, srlu: 6039, qtsrlu: 38151 },
        { hour: 0, min: 45, srlu: 5354, qtsrlu: 35583 },
        { hour: 1, min: 0, srlu: 4772, qtsrlu: 32384 },
        { hour: 1, min: 15, srlu: 4403, qtsrlu: 31065 },
        { hour: 1, min: 30, srlu: 3710, qtsrlu: 28320 },
        { hour: 1, min: 45, srlu: 3520, qtsrlu: 28048 },
        { hour: 2, min: 0, srlu: 3336, qtsrlu: 26018 },
        { hour: 2, min: 15, srlu: 2982, qtsrlu: 26246 },
        { hour: 2, min: 30, srlu: 2748, qtsrlu: 23063 },
        { hour: 2, min: 45, srlu: 2643, qtsrlu: 23487 },
        { hour: 3, min: 0, srlu: 2839, qtsrlu: 25060 },
        { hour: 3, min: 15, srlu: 2819, qtsrlu: 24633 },
        { hour: 3, min: 30, srlu: 2736, qtsrlu: 26299 },
        { hour: 3, min: 45, srlu: 2864, qtsrlu: 25945 },
        { hour: 4, min: 0, srlu: 3034, qtsrlu: 25336 },
        { hour: 4, min: 15, srlu: 3576, qtsrlu: 28679 },
        { hour: 4, min: 30, srlu: 4559, qtsrlu: 36537 },
        { hour: 4, min: 45, srlu: 5502, qtsrlu: 38620 },
        { hour: 5, min: 0, srlu: 7138, qtsrlu: 51665 },
        { hour: 5, min: 15, srlu: 9525, qtsrlu: 65519 },
        { hour: 5, min: 30, srlu: 12391, qtsrlu: 84939 },
        { hour: 5, min: 45, srlu: 16244, qtsrlu: 107746 },
        { hour: 6, min: 0, srlu: 19487, qtsrlu: 135611 },
        { hour: 6, min: 15, srlu: 23253, qtsrlu: 175957 },
        { hour: 6, min: 30, srlu: 24988, qtsrlu: 202922 },
        { hour: 6, min: 45, srlu: 28106, qtsrlu: 222049 },
        { hour: 7, min: 0, srlu: 32075, qtsrlu: 250548 },
        { hour: 7, min: 15, srlu: 37966, qtsrlu: 298193 },
        { hour: 7, min: 30, srlu: 46288, qtsrlu: 362202 },
        { hour: 7, min: 45, srlu: 52840, qtsrlu: 416267 },
        { hour: 8, min: 0, srlu: 56922, qtsrlu: 465006 },
        { hour: 8, min: 15, srlu: 61129, qtsrlu: 491590 },
        { hour: 8, min: 30, srlu: 61412, qtsrlu: 507964 },
        { hour: 8, min: 45, srlu: 53320, qtsrlu: 407734 },
        { hour: 9, min: 0, srlu: 48714, qtsrlu: 355882 },
        { hour: 9, min: 15, srlu: 47325, qtsrlu: 363459 },
        { hour: 9, min: 30, srlu: 48582, qtsrlu: 377763 },
        { hour: 9, min: 45, srlu: 48684, qtsrlu: 374762 },
        { hour: 10, min: 0, srlu: 46434, qtsrlu: 374296 },
        { hour: 10, min: 15, srlu: 47836, qtsrlu: 386144 },
        { hour: 10, min: 30, srlu: 47561, qtsrlu: 391961 },
        { hour: 10, min: 45, srlu: 46199, qtsrlu: 386969 },
        { hour: 11, min: 0, srlu: 46098, qtsrlu: 384278 },
        { hour: 11, min: 15, srlu: 45327, qtsrlu: 374822 },
        { hour: 11, min: 30, srlu: 45359, qtsrlu: 382730 },
        { hour: 11, min: 45, srlu: 43834, qtsrlu: 374387 },
        { hour: 12, min: 0, srlu: 41731, qtsrlu: 365221 },
        { hour: 12, min: 15, srlu: 40722, qtsrlu: 340226 },
        { hour: 12, min: 30, srlu: 40931, qtsrlu: 347980 },
        { hour: 12, min: 45, srlu: 40512, qtsrlu: 343209 },
        { hour: 13, min: 0, srlu: 42142, qtsrlu: 356466 },
        { hour: 13, min: 15, srlu: 42939, qtsrlu: 363887 },
        { hour: 13, min: 30, srlu: 44283, qtsrlu: 373709 },
        { hour: 13, min: 45, srlu: 44084, qtsrlu: 376553 },
        { hour: 14, min: 0, srlu: 44586, qtsrlu: 379939 },
        { hour: 14, min: 15, srlu: 45850, qtsrlu: 396031 },
        { hour: 14, min: 30, srlu: 46859, qtsrlu: 402091 },
        { hour: 14, min: 45, srlu: 46176, qtsrlu: 390236 },
        { hour: 15, min: 0, srlu: 46058, qtsrlu: 402176 },
        { hour: 15, min: 15, srlu: 45817, qtsrlu: 396058 },
        { hour: 15, min: 30, srlu: 46506, qtsrlu: 402133 },
        { hour: 15, min: 45, srlu: 47186, qtsrlu: 398417 },
        { hour: 16, min: 0, srlu: 47494, qtsrlu: 419063 },
        { hour: 16, min: 15, srlu: 50400, qtsrlu: 430158 },
        { hour: 16, min: 30, srlu: 50332, qtsrlu: 443554 },
        { hour: 16, min: 45, srlu: 51817, qtsrlu: 454162 },
        { hour: 17, min: 0, srlu: 53698, qtsrlu: 480895 },
        { hour: 17, min: 15, srlu: 53020, qtsrlu: 485439 },
        { hour: 17, min: 30, srlu: 53996, qtsrlu: 502179 },
        { hour: 17, min: 45, srlu: 53065, qtsrlu: 501848 },
        { hour: 18, min: 0, srlu: 56856, qtsrlu: 568259 },
        { hour: 18, min: 15, srlu: 57575, qtsrlu: 582391 },
        { hour: 18, min: 30, srlu: 53956, qtsrlu: 519675 },
        { hour: 18, min: 45, srlu: 47516, qtsrlu: 417347 },
        { hour: 19, min: 0, srlu: 42840, qtsrlu: 351477 },
        { hour: 19, min: 15, srlu: 37236, qtsrlu: 294932 },
        { hour: 19, min: 30, srlu: 32726, qtsrlu: 252080 },
        { hour: 19, min: 45, srlu: 29244, qtsrlu: 222885 },
        { hour: 20, min: 0, srlu: 28693, qtsrlu: 214882 },
        { hour: 20, min: 15, srlu: 27658, qtsrlu: 213025 },
        { hour: 20, min: 30, srlu: 26128, qtsrlu: 195232 },
        { hour: 20, min: 45, srlu: 25152, qtsrlu: 183621 },
        { hour: 21, min: 0, srlu: 25062, qtsrlu: 190691 },
        { hour: 21, min: 15, srlu: 24236, qtsrlu: 176260 },
        { hour: 21, min: 30, srlu: 22349, qtsrlu: 161975 },
        { hour: 21, min: 45, srlu: 21292, qtsrlu: 153163 },
        { hour: 22, min: 0, srlu: 20586, qtsrlu: 144773 },
        { hour: 22, min: 15, srlu: 19623, qtsrlu: 137587 },
        { hour: 22, min: 30, srlu: 17084, qtsrlu: 118409 },
        { hour: 22, min: 45, srlu: 16009, qtsrlu: 105872 },
        { hour: 23, min: 0, srlu: 14958, qtsrlu: 103744 },
        { hour: 23, min: 15, srlu: 13769, qtsrlu: 89671 },
        { hour: 23, min: 30, srlu: 11811, qtsrlu: 75320 },
        { hour: 23, min: 45, srlu: 9682, qtsrlu: 62029 },
    ],
};

jest.mock(
    "react-apexcharts",
    () =>
        ({ options, series, type, width, height }) => {
            // console.log("options", options);
            // console.log("series", series);
            // console.log("type", type);
            // console.log("width", width);
            // console.log("height", height);
            return (
                <div>
                    Chart
                    <div data-testid='type'>{type}</div>
                    <div data-testid='series-data-name'>{series[0]?.name}</div>
                    <div data-testid='series-data-length'>
                        {series[0]?.data?.length}
                    </div>
                    <div data-testid='series-lastweek-data-name'>
                        {series[1]?.name}
                    </div>
                    <div data-testid='series-lastweek-data-length'>
                        {series[1]?.data?.length}
                    </div>
                    <div data-testid='series-lastmonth-data-name'>
                        {series[2]?.name}
                    </div>
                    <div data-testid='series-lastmonth-data-length'>
                        {series[2]?.data?.length}
                    </div>
                </div>
            );
        }
);

describe("chart test", () => {
    beforeEach(() => {
        render(
            <ChartMfd
                dataMfd={dataMfd}
                dataLastWeekMfd={dataLastWeekMfd}
                dataLastMonthAvgMfd={dataLastMonthAvgMfd}
            />
        );
    });

    test("check type", () => {
        const element = screen.getByTestId("type");
        expect(element.innerHTML).toEqual("line");
    });

    test("check data", () => {
        const elementName = screen.getByTestId("series-data-name");
        expect(elementName.innerHTML).toEqual("2022-4-5");

        const elementLength = screen.getByTestId("series-data-length");
        expect(elementLength.innerHTML).toEqual(dataMfd.data.length.toString());
    });

    test("check lastweek data", () => {
        const elementName = screen.getByTestId("series-lastweek-data-name");
        expect(elementName.innerHTML).toEqual("lastWeek");

        const elementLength = screen.getByTestId("series-lastweek-data-length");
        expect(elementLength.innerHTML).toEqual("1");
    });

    test("check lastmonth data", () => {
        const elementName = screen.getByTestId("series-lastmonth-data-name");
        expect(elementName.innerHTML).toEqual("lastMonth");

        const elementLength = screen.getByTestId(
            "series-lastmonth-data-length"
        );
        expect(elementLength.innerHTML).toEqual("1");
    });
});
