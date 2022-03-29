import React from 'react'
import { Link } from 'react-router-dom'

import styles from './MenuGroup.module.css'

const menuData = [
    {
        title: "대시보드",
        items: ["교통현황", "교통현황 데이터"]
    },
    {
        title: "통계",
        items: ["교통통계", "교통통계 데이터", "긴급차량 통계"]
    },
    {
        title:"관리",
        items: ["카메라 관리", "교차로 관리", "구역 관리", "긴급차량 관리", "사용자 관리"]
    }
]

function MenuGroup({ children }) {
    
    const menuElements = menuData.map(group => {
        const itemElements = group.map(item => {
            return (
                <li>{item}</li>
            )
        })
        
        return (
            <li>
                <div className={styles.title}>{group.title}</div>
                <ul>
                    {itemElements}
                </ul>
            </li>            
        )
    })
    return (
        <div>
            {menuElements}
        </div>
    )
}

export default MenuGroup